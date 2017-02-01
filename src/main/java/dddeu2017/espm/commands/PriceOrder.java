package dddeu2017.espm.commands;

import dddeu2017.espm.framework.MessageBase;

import java.util.UUID;

public class PriceOrder extends MessageBase {

    public PriceOrder(UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
    }
}
