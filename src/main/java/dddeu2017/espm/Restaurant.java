package dddeu2017.espm;

import dddeu2017.espm.framework.RoundRobin;
import dddeu2017.espm.framework.ThreadedHandler;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private static List<ThreadedHandler> threads = new ArrayList<>();

    public static void main(String[] args) {
        // wire up the system
        HandlerOrder printer = threaded(new OrderPrinter());
        HandlerOrder cashier = threaded(new Cashier(printer));
        HandlerOrder assistantManager = threaded(new AssistantManager(cashier));
        HandlerOrder cooks = threaded(new RoundRobin(
                threaded(new Cook("Tom", assistantManager)),
                threaded(new Cook("Dick", assistantManager)),
                threaded(new Cook("Harry", assistantManager))
        ));
        Waiter waiter = new Waiter(cooks);

        // start the system
        for (ThreadedHandler thread : threads) {
            thread.start();
        }

        // use the system
        for (int i = 0; i < 10; i++) {
            waiter.placeOrder();
        }
    }

    private static HandlerOrder threaded(HandlerOrder handler) {
        ThreadedHandler th = new ThreadedHandler(handler);
        threads.add(th);
        return th;
    }
}
