package dddeu2017.espm;

import dddeu2017.espm.framework.RoundRobin;
import dddeu2017.espm.framework.ThreadedHandler;
import dddeu2017.espm.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Restaurant {

    private static final Logger log = LoggerFactory.getLogger(Restaurant.class);

    private static List<ThreadedHandler> threads = new ArrayList<>();

    public static void main(String[] args) {
        // wire up the system
        HandlerOrder printer = threaded("OrderPrinter", new OrderPrinter());
        HandlerOrder cashier = threaded("Cashier", new Cashier(printer));
        HandlerOrder assistantManager = threaded("AssistantManager", new AssistantManager(cashier));
        HandlerOrder cooks = threaded("RoundRobin Cook", new RoundRobin(
                threaded("Cook Tom", new Cook("Tom", assistantManager)),
                threaded("Cook Dick", new Cook("Dick", assistantManager)),
                threaded("Cook Harry", new Cook("Harry", assistantManager))
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

        while (true) {
            Util.sleep(1000);
            statusReport();
        }
    }

    private static void statusReport() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Status report");
        for (ThreadedHandler thread : threads) {
            sj.add("\t" + thread.getCount() + "\t" + thread.getName());
        }
        log.info(sj.toString());
    }

    private static HandlerOrder threaded(String name, HandlerOrder handler) {
        ThreadedHandler th = new ThreadedHandler(name, handler);
        threads.add(th);
        return th;
    }
}
