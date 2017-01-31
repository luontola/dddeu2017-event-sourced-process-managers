package dddeu2017.espm;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        StopWatch stopWatch = StopWatch.createStarted();
        OrderPrinter printer = new OrderPrinter();
        Cashier cashier = new Cashier(printer);
        AssistantManager assistantManager = new AssistantManager(cashier);
        Cook cook = new Cook(assistantManager);
        Waiter waiter = new Waiter(cook);
        for (int i = 0; i < 10; i++) {
            waiter.placeOrder();
        }
        log.info("Took {} seconds", stopWatch.getTime(TimeUnit.MILLISECONDS) / 1000.0);
    }
}
