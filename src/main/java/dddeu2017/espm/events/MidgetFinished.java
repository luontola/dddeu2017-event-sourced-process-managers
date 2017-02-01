package dddeu2017.espm.events;

import dddeu2017.espm.framework.MessageBase;

import java.util.UUID;

public class MidgetFinished extends MessageBase {

    public MidgetFinished(UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
    }
}
