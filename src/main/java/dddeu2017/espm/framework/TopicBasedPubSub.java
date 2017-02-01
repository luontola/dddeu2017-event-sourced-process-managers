package dddeu2017.espm.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TopicBasedPubSub implements Publisher {

    private static final Logger log = LoggerFactory.getLogger(TopicBasedPubSub.class);

    private static final Method handleMethod;

    static {
        try {
            handleMethod = Handler.class.getMethod("handle", Object.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private final Map<String, List<Handler<?>>> handlersByTopic = new ConcurrentHashMap<>();
    private final Map<String, List<MessageBase>> historyByTopic = new ConcurrentHashMap<>();

    public <T> void subscribe(Class<T> messageType, Handler<? super T> handler) {
        subscribe(messageType.getName(), handler);
    }

    public void subscribe(UUID correlationId, Handler<?> handler) {
        subscribe(correlationId.toString(), handler);
    }

    private void subscribe(String topic, Handler<?> handler) {
        // CopyOnWriteArrayList because subscribe is called rarely, but publish is called a lot
        handlersByTopic.computeIfAbsent(topic, key -> new CopyOnWriteArrayList<>())
                .add(handler);
    }

    @Override
    public <T extends MessageBase> void publish(T message) {
        publish(message.getClass().getName(), message);
        publish(message.correlationId.toString(), message);
    }

    private void publish(String topic, MessageBase message) {
        List<Handler<?>> handlers = handlersByTopic.getOrDefault(topic, Collections.emptyList());
        for (Handler<?> handler : handlers) {
            try {
                handleMethod.invoke(handler, message);
            } catch (ReflectiveOperationException e) {
                log.error("Failed to publish", e);
            }
        }
        List<MessageBase> history = historyByTopic.computeIfAbsent(topic, key -> new ArrayList<>());
        synchronized (history) {
            history.add(message);
        }
    }

    public List<MessageBase> historyForTopic(UUID correlationId) {
        return historyForTopic(correlationId.toString());
    }

    private List<MessageBase> historyForTopic(String topic) {
        return new ArrayList<>(historyByTopic.getOrDefault(topic, Collections.emptyList()));
    }
}
