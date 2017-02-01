package dddeu2017.espm.commands;

import dddeu2017.espm.Order;
import dddeu2017.espm.framework.Expirable;
import dddeu2017.espm.framework.MessageBase;

import java.time.Instant;
import java.util.UUID;

public class CookFood extends MessageBase implements Expirable {

    public final Order order;

    public CookFood(Order order, UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
        this.order = order;
    }

    @Override
    public Instant expires() {
        return order.expires;
    }
}
