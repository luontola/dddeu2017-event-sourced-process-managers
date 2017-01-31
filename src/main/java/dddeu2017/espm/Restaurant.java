package dddeu2017.espm;

import dddeu2017.espm.framework.RoundRobin;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Restaurant {

    private static final Logger log = LoggerFactory.getLogger(Restaurant.class);

    public static void main(String[] args) {
        StopWatch stopWatch = StopWatch.createStarted();
        OrderPrinter printer = new OrderPrinter();
        Cashier cashier = new Cashier(printer);
        AssistantManager assistantManager = new AssistantManager(cashier);
        HandlerOrder cooks = new RoundRobin(
                new Cook("Tom", assistantManager),
                new Cook("Dick", assistantManager),
                new Cook("Harry", assistantManager)
        );
        Waiter waiter = new Waiter(cooks);
        for (int i = 0; i < 10; i++) {
            waiter.placeOrder();
        }
        log.info("Took {} seconds", stopWatch.getTime(TimeUnit.MILLISECONDS) / 1000.0);
    }
}
