package dddeu2017.espm.framework;

import dddeu2017.espm.Handler;
import dddeu2017.espm.OrderHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
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

    private final Map<String, List<OrderHandler>> topics = new ConcurrentHashMap<>();

    public <T> void subscribe(Class<T> topic, OrderHandler handler) {
        subscribe(topic.getName(), handler);
    }

    private void subscribe(String topic, OrderHandler handler) {
        // CopyOnWriteArrayList because subscribe is called rarely, but publish is called a lot
        topics.computeIfAbsent(topic, key -> new CopyOnWriteArrayList<>())
                .add(handler);
    }

    @Override
    public <T> void publish(T message) {
        publish(message.getClass().getName(), message);
    }

    private void publish(String topic, Object message) {
        List<OrderHandler> handlers = topics.get(topic);
        for (OrderHandler handler : handlers) {
            try {
                Field orderField = message.getClass().getDeclaredField("order");
                Object order = orderField.get(message);
                handleMethod.invoke(handler, order);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
