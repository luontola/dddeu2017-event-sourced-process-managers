package dddeu2017.espm.commands;

import dddeu2017.espm.framework.MessageBase;

import java.util.UUID;

public class TakePayment extends MessageBase {

    public TakePayment(UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
    }
}
