package dddeu2017.espm.framework;

import dddeu2017.espm.Order;
import dddeu2017.espm.OrderHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TopicBasedPubSub implements Publisher {

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
    public <T> void publish(Class<T> topic, Order order) {
        publish(topic.getName(), order);
    }

    private void publish(String topic, Order order) {
        List<OrderHandler> handlers = topics.get(topic);
        for (OrderHandler handler : handlers) {
            handler.handle(order);
        }
    }
}
