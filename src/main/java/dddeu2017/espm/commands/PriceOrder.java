package dddeu2017.espm.commands;

import dddeu2017.espm.Order;
import dddeu2017.espm.framework.MessageBase;

import java.util.UUID;

public class PriceOrder extends MessageBase {

    public final Order order;

    public PriceOrder(Order order, UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
        this.order = order;
    }
}
