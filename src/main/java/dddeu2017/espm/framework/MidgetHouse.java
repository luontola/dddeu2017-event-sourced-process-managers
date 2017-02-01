package dddeu2017.espm.framework;

import dddeu2017.espm.Midget;
import dddeu2017.espm.events.OrderPlaced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MidgetHouse implements Handler<MessageBase> {

    private static final Logger log = LoggerFactory.getLogger(MidgetHouse.class);

    private final Map<UUID, Midget> midgetsByCorrelationId = new HashMap<>();
    private final TopicBasedPubSub topics;
    private Handler<MessageBase> self;

    public MidgetHouse(TopicBasedPubSub topics) {
        this.topics = topics;
    }

    public void setSelf(Handler<MessageBase> self) {
        this.self = self;
    }

    @Override
    public void handle(MessageBase message) {
        log.trace("Handle message id={} correlationId={} causationId={}\n{}", message.id, message.correlationId, message.causationId, message);
        if (message instanceof OrderPlaced) {
            subscribeNewMidget((OrderPlaced) message);
        }
        Midget midget = midgetsByCorrelationId.get(message.correlationId);
        midget.handle(message);
    }

    private void subscribeNewMidget(OrderPlaced message) {
        midgetsByCorrelationId.put(message.correlationId, new Midget(topics));
        topics.subscribe(message.correlationId, self);
    }
}
