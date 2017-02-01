package dddeu2017.espm.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class Flaky<T> implements Handler<T> {

    private static final Logger log = LoggerFactory.getLogger(Flaky.class);

    private final Handler<T> target;

    public Flaky(Handler<T> target) {
        this.target = target;
    }

    @Override
    public void handle(T message) {
        MessageBase m = (MessageBase) message;
        double random = ThreadLocalRandom.current().nextDouble();
        if (random < 0.1) {
            // drop the message
            log.info("[{}] Dropping message {}", m.correlationId, m.getClass().getSimpleName());
        } else if (random < 0.2) {
            // duplicate the message
            log.info("[{}] Duplicating message {}", m.correlationId, m.getClass().getSimpleName());
            target.handle(message);
            target.handle(message);
        } else {
            // normal
            target.handle(message);
        }
    }
}
