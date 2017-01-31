package dddeu2017.espm.framework;

import dddeu2017.espm.HandlerOrder;
import dddeu2017.espm.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class TtlChecker implements HandlerOrder {

    private static final Logger log = LoggerFactory.getLogger(TtlChecker.class);

    private final HandlerOrder handler;

    public TtlChecker(HandlerOrder handler) {
        this.handler = handler;
    }

    @Override
    public void handle(Order order) {
        if (Instant.now().isAfter(order.expires)) {
            log.info("Dropping expired order");
        } else {
            handler.handle(order);
        }
    }
}
