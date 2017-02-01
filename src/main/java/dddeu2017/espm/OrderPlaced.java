package dddeu2017.espm;

import dddeu2017.espm.framework.Expirable;
import dddeu2017.espm.framework.MessageBase;

import java.time.Instant;
import java.util.UUID;

public class OrderPlaced extends MessageBase implements Expirable {

    public final Order order;

    public OrderPlaced(Order order, UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
        this.order = order;
    }

    @Override
    public Instant expires() {
        return order.expires;
    }
}
