package dddeu2017.espm.framework;

import dddeu2017.espm.OrderPlaced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

// TODO: make the message type generic
public class TtlChecker implements Handler<OrderPlaced> {

    private static final Logger log = LoggerFactory.getLogger(TtlChecker.class);

    private final Handler<OrderPlaced> handler;

    public TtlChecker(Handler<OrderPlaced> handler) {
        this.handler = handler;
    }

    @Override
    public void handle(OrderPlaced message) {
        if (Instant.now().isAfter(message.order.expires)) {
            log.info("Dropping expired order");
        } else {
            handler.handle(message);
        }
    }
}
