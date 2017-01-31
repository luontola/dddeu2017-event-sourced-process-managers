package dddeu2017.espm;

import dddeu2017.espm.framework.Publisher;
import dddeu2017.espm.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cashier implements OrderHandler {

    private static final Logger log = LoggerFactory.getLogger(Cashier.class);

    private final Publisher publisher;

    public Cashier(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(Order order) {
        log.info("Taking your money");
        Util.sleep(500);
        order.paid = true;
        publisher.publish(OrderPaid.class, order);
    }
}
