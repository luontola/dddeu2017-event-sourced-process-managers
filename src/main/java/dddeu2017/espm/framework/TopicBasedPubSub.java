package dddeu2017.espm.framework;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TopicBasedPubSub implements Publisher {

    private static final Method handleMethod;

    static {
        try {
            handleMethod = Handler.class.getMethod("handle", Object.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private final Map<String, List<Handler<?>>> topics = new ConcurrentHashMap<>();

    public <T> void subscribe(Class<T> messageType, Handler<? super T> handler) {
        subscribe(messageType.getName(), handler);
    }

    public void subscribe(UUID correlationId, Handler<?> handler) {
        subscribe(correlationId.toString(), handler);
    }

    private void subscribe(String topic, Handler<?> handler) {
        // CopyOnWriteArrayList because subscribe is called rarely, but publish is called a lot
        topics.computeIfAbsent(topic, key -> new CopyOnWriteArrayList<>())
                .add(handler);
    }

    @Override
    public <T extends MessageBase> void publish(T message) {
        publish(message.getClass().getName(), message);
        publish(message.correlationId.toString(), message);
    }

    private void publish(String topic, Object message) {
        List<Handler<?>> handlers = topics.getOrDefault(topic, Collections.emptyList());
        for (Handler<?> handler : handlers) {
            try {
                handleMethod.invoke(handler, message);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
