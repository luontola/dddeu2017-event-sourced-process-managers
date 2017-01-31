package dddeu2017.espm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderPrinter implements OrderHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderPrinter.class);

    @Override
    public void handle(Order order) {
        log.info("{}", order);
    }
}
