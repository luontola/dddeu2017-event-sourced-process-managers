package dddeu2017.espm.events;

import dddeu2017.espm.Order;
import dddeu2017.espm.framework.MessageBase;

import java.util.UUID;

public class OrderCooked extends MessageBase {

    public final Order order;

    public OrderCooked(Order order, UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
        this.order = order;
    }
}
