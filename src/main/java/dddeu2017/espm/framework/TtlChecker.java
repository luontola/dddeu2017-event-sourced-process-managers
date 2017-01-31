package dddeu2017.espm.framework;

import dddeu2017.espm.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.Instant;

public class TtlChecker<T> implements Handler<T> {

    private static final Logger log = LoggerFactory.getLogger(TtlChecker.class);

    private final Handler<T> handler;

    public TtlChecker(Handler<T> handler) {
        this.handler = handler;
    }

    @Override
    public void handle(T message) {
        if (Instant.now().isAfter(expires(message))) {
            log.info("Dropping expired order");
        } else {
            handler.handle(message);
        }
    }

    private Instant expires(T message) {
        try {
            // XXX: assumes the existence of an order field in the message
            Field orderField = message.getClass().getField("order");
            Order order = (Order) orderField.get(message);
            return order.expires;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
