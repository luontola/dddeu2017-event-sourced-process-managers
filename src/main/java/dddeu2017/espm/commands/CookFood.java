package dddeu2017.espm.commands;

import dddeu2017.espm.Order;
import dddeu2017.espm.framework.MessageBase;

import java.util.UUID;

public class CookFood extends MessageBase {

    public final Order order;

    public CookFood(Order order, UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
        this.order = order;
    }
}
