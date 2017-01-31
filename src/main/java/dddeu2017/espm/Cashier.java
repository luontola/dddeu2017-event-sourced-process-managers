package dddeu2017.espm;

import dddeu2017.espm.framework.Handler;
import dddeu2017.espm.framework.Publisher;
import dddeu2017.espm.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cashier implements Handler<OrderPriced> {

    private static final Logger log = LoggerFactory.getLogger(Cashier.class);

    private final Publisher publisher;

    public Cashier(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(OrderPriced message) {
        log.info("Taking your money");
        Order order = message.order;
        Util.sleep(500);
        order.paid = true;
        publisher.publish(new OrderPaid(order));
    }
}
