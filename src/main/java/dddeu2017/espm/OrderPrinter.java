package dddeu2017.espm;

import dddeu2017.espm.framework.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderPrinter implements Handler<OrderPaid> {

    private static final Logger log = LoggerFactory.getLogger(OrderPrinter.class);

    @Override
    public void handle(OrderPaid message) {
        log.info("{}", message.order);
    }
}
