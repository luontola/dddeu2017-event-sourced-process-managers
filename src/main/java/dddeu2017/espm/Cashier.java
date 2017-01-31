package dddeu2017.espm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cashier implements HandlerOrder {

    private static final Logger log = LoggerFactory.getLogger(Cashier.class);

    private final HandlerOrder next;

    public Cashier(HandlerOrder next) {
        this.next = next;
    }

    @Override
    public void handle(Order order) {
        log.info("Taking your money");
        Util.sleep(500);
        order.paid = true;
        next.handle(order);
    }
}
