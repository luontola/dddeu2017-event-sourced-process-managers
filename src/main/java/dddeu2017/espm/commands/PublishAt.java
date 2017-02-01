package dddeu2017.espm.commands;

import dddeu2017.espm.framework.MessageBase;

import java.time.Instant;
import java.util.UUID;

public class PublishAt extends MessageBase {

    public final Instant when;
    public final MessageBase message;

    public PublishAt(Instant when, MessageBase message, UUID correlationId, UUID causationId) {
        super(correlationId, causationId);
        this.when = when;
        this.message = message;
    }
}
