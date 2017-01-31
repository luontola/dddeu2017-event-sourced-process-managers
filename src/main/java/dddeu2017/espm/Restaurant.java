package dddeu2017.espm;

import dddeu2017.espm.framework.Handler;
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
        Waiter waiter = new Waiter(topics);
        Handler<Order> cooks = threaded("Dispatch to Cooks", new MoreFair<>(Arrays.asList(
                threaded("Cook Tom", new TtlChecker(new Cook("Tom", randomCookTime(), topics))),
                threaded("Cook Dick", new TtlChecker(new Cook("Dick", randomCookTime(), topics))),
                threaded("Cook Harry", new TtlChecker(new Cook("Harry", randomCookTime(), topics)))
        )));
        Handler<Order> assistantManager = threaded("AssistantManager", new AssistantManager(topics));
        Handler<Order> cashier = threaded("Cashier", new Cashier(topics));
        Handler<Order> printer = threaded("OrderPrinter", new OrderPrinter());

        // subscribe
        topics.subscribe(OrderPlaced.class, cooks);
        topics.subscribe(OrderCooked.class, assistantManager);
        topics.subscribe(OrderPriced.class, cashier);
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
