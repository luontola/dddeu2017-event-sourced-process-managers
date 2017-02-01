package dddeu2017.espm.framework;

import dddeu2017.espm.Midget;
import dddeu2017.espm.events.OrderPlaced;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MidgetHouse implements Handler<MessageBase> {

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
