package dddeu2017.espm;

import dddeu2017.espm.framework.MessageBase;

import java.util.UUID;

public class OrderPaid extends MessageBase {

    public final Order order;

    public OrderPaid(Order order, UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
        this.order = order;
    }
}
