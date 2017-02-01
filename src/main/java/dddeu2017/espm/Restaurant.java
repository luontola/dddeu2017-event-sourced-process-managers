package dddeu2017.espm;

import dddeu2017.espm.commands.CookFood;
import dddeu2017.espm.commands.PriceOrder;
import dddeu2017.espm.commands.PublishAt;
import dddeu2017.espm.commands.TakePayment;
import dddeu2017.espm.events.OrderPaid;
import dddeu2017.espm.events.OrderPlaced;
import dddeu2017.espm.framework.AlarmClock;
import dddeu2017.espm.framework.Handler;
import dddeu2017.espm.framework.MessageBase;
import dddeu2017.espm.framework.MidgetHouse;
import dddeu2017.espm.framework.MoreFair;
import dddeu2017.espm.framework.ThreadedHandler;
import dddeu2017.espm.framework.TopicBasedPubSub;
import dddeu2017.espm.framework.TtlChecker;
import dddeu2017.espm.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

public class Restaurant {

    private static final Logger log = LoggerFactory.getLogger(Restaurant.class);

    private static List<ThreadedHandler> threads = new ArrayList<>();

    public static void main(String[] args) {
        // create
        TopicBasedPubSub topics = new TopicBasedPubSub();
        MidgetHouse rawMidgetHouse = new MidgetHouse(topics);
        Handler<MessageBase> midgetHouse = threaded("MidgetHouse", rawMidgetHouse);
        rawMidgetHouse.setSelf(midgetHouse);
        Handler<PublishAt> alarmClock = threaded("AlarmClock", new AlarmClock(topics));
        Waiter waiter = new Waiter(topics);
        Handler<CookFood> cooks = threaded("Dispatch to Cooks", new MoreFair<>(Arrays.asList(
                threaded("Cook Tom", new TtlChecker<>(new Cook("Tom", 1500, topics))),
                threaded("Cook Dick", new TtlChecker<>(new Cook("Dick", 2500, topics))),
                threaded("Cook Harry", new TtlChecker<>(new Cook("Harry", 3000, topics)))
        )));
        Handler<PriceOrder> assistantManager = threaded("AssistantManager", new AssistantManager(topics));
        Handler<TakePayment> cashier = threaded("Cashier", new Cashier(topics));
        Handler<OrderPaid> printer = threaded("OrderPrinter", new OrderPrinter());

        // subscribe
        topics.subscribe(OrderPlaced.class, midgetHouse);
        topics.subscribe(PublishAt.class, alarmClock);
        topics.subscribe(CookFood.class, cooks);
        topics.subscribe(PriceOrder.class, assistantManager);
        topics.subscribe(TakePayment.class, cashier);
        topics.subscribe(OrderPaid.class, printer);

        // start
        for (ThreadedHandler thread : threads) {
            thread.start();
        }

        // steady
        for (int i = 0; i < 100; i++) {
            waiter.placeOrder();
        }
        do {
            Util.sleep(1000);
            statusReport();
        } while (!idle());
    }

    private static int randomCookTime() {
        return ThreadLocalRandom.current().nextInt(1000, 3000);
    }

    private static void statusReport() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Status report");
        for (ThreadedHandler thread : threads) {
            sj.add("\t" + thread.getCount() + "\t" + thread.getName());
        }
        log.info(sj.toString());
    }

    private static boolean idle() {
        return threads.stream()
                .mapToInt(ThreadedHandler::getCount)
                .sum() == 0;
    }

    private static <T> ThreadedHandler<T> threaded(String name, Handler<T> handler) {
        ThreadedHandler<T> th = new ThreadedHandler<>(name, handler);
        threads.add(th);
        return th;
    }
}
