package dddeu2017.espm.framework;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

import static dddeu2017.espm.util.Util.newUUID;

public abstract class MessageBase {

    public final UUID id = newUUID();
    public final UUID correlationId;
    public final UUID causationId;

    public MessageBase(UUID correlationId, UUID causationId) {
        this.correlationId = correlationId;
        this.causationId = causationId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
