package dddeu2017.espm.framework;

import java.util.UUID;

public abstract class MessageBase {
    public final UUID id = UUID.randomUUID();
    public final UUID correlationId;
    public final UUID causationId;

    public MessageBase(UUID correlationId, UUID causationId) {
        this.correlationId = correlationId;
        this.causationId = causationId;
    }
}
