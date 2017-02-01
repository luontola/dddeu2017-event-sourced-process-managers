package dddeu2017.espm.commands;

import dddeu2017.espm.framework.MessageBase;

import java.util.UUID;

public class CookFood extends MessageBase {

    public CookFood(UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
    }
}
