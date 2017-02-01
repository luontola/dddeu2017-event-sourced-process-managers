package dddeu2017.espm.framework;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import dddeu2017.espm.EatFirstMidget;
import dddeu2017.espm.PayFirstMidget;
import dddeu2017.espm.events.MidgetFinished;
import dddeu2017.espm.events.OrderPlaced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MidgetHouse implements Handler<MessageBase> {

    private static final Logger log = LoggerFactory.getLogger(MidgetHouse.class);

    private final Map<UUID, Class<? extends Midget>> midgetTypesByCorrelationId = new HashMap<>();
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
        log.trace("Handle message {} id={} correlationId={} causationId={}",
                message.getClass().getSimpleName(), message.id, message.correlationId, message.causationId);
        if (message instanceof OrderPlaced) {
            subscribeNewMidget((OrderPlaced) message);
        }
        delegateToMidget(message);
        if (message instanceof MidgetFinished) {
            killMidget((MidgetFinished) message);
        }
    }

    private void subscribeNewMidget(OrderPlaced message) {
        Midget midget = createMidget(message);
        log.trace("Using {} for correlationId={}", midget.getClass().getSimpleName(), message.correlationId);
        midgetTypesByCorrelationId.put(message.correlationId, midget.getClass());
        topics.subscribe(message.correlationId, self);
    }

    private Midget createMidget(OrderPlaced message) {
        // XXX: in a generic framework you would extract this into a MidgetFactory
        return message.order.dodgy ? new PayFirstMidget() : new EatFirstMidget();
    }

    private void delegateToMidget(MessageBase message) {
        List<MessageBase> history = topics.historyForTopic(message.correlationId);
        Class<? extends Midget> midgetType = midgetTypesByCorrelationId.get(message.correlationId);
        if (midgetType != null) {
            Midget midget = loadMidget(midgetType, history);
            midget.handle(message);
        } else {
            log.trace("No midget for message {}", message);
        }
    }

    private Midget loadMidget(Class<? extends Midget> midgetType, List<MessageBase> history) {
        Midget midget;
        try {
            midget = midgetType.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        // silence the logger
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger midgetLogger = lc.getLogger(midgetType);
        Level originalLevel = midgetLogger.getLevel();
        midgetLogger.setLevel(Level.OFF);
        try {
            // replay old events
            midget.setPublisher(new NullPublisher());
            history.forEach(midget::handle);
            midget.setPublisher(topics);
        } finally {
            // restore the logger
            midgetLogger.setLevel(originalLevel);
        }
        return midget;
    }

    private void killMidget(MidgetFinished message) {
        midgetTypesByCorrelationId.remove(message.correlationId);
    }
}
