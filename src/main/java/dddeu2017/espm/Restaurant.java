package dddeu2017.espm;

import dddeu2017.espm.framework.MoreFair;
import dddeu2017.espm.framework.ThreadedHandler;
import dddeu2017.espm.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

public class Restaurant {

    private static final Logger log = LoggerFactory.getLogger(Restaurant.class);

    private static List<ThreadedHandler> threads = new ArrayList<>();

    public static void main(String[] args) {
        // wire up the system
        HandlerOrder printer = threaded("OrderPrinter", new OrderPrinter());
        HandlerOrder cashier = threaded("Cashier", new Cashier(printer));
        HandlerOrder assistantManager = threaded("AssistantManager", new AssistantManager(cashier));
        HandlerOrder cooks = threaded("Dispatch to Cooks", new MoreFair(
                threaded("Cook Tom", new Cook("Tom", randomCookTime(), assistantManager)),
                threaded("Cook Dick", new Cook("Dick", randomCookTime(), assistantManager)),
                threaded("Cook Harry", new Cook("Harry", randomCookTime(), assistantManager))
        ));
        Waiter waiter = new Waiter(cooks);

        // start the system
        for (ThreadedHandler thread : threads) {
            thread.start();
        }

        // use the system
        for (int i = 0; i < 100; i++) {
            waiter.placeOrder();
        }

        while (!idle()) {
            Util.sleep(1000);
            statusReport();
        }
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

    private static ThreadedHandler threaded(String name, HandlerOrder handler) {
        ThreadedHandler th = new ThreadedHandler(name, handler);
        threads.add(th);
        return th;
    }
}
