package dddeu2017.espm.framework;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobin<T> implements Handler<T> {

    private final Queue<Handler<T>> handlers;

    public RoundRobin(List<Handler<T>> handlers) {
        this.handlers = new LinkedList<>(handlers);
    }

    @Override
    public void handle(T message) {
        handlers.peek().handle(message);
        handlers.add(handlers.remove());
    }
}
