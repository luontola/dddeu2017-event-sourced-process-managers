package dddeu2017.espm.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            log.info("[{}] Dropping expired order", ((MessageBase) message).correlationId);
        } else {
            handler.handle(message);
        }
    }

    private Instant expires(T message) {
        if (message instanceof Expirable) {
            Expirable m = (Expirable) message;
            return m.expires();
        } else {
            return Instant.MAX;
        }
    }
}
