package dddeu2017.espm.framework;

import dddeu2017.espm.util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MoreFair<T> implements Handler<T> {

    private final Queue<ThreadedHandler<T>> handlers;

    public MoreFair(List<ThreadedHandler<T>> handlers) {
        this.handlers = new LinkedList<>(handlers);
    }

    @Override
    public void handle(T message) {
        while (true) {
            ThreadedHandler<T> handler = nextHandler();
            if (handler.getCount() < 5) {
                handler.handle(message);
                return;
            }
            Util.sleep(1);
        }
    }

    private ThreadedHandler<T> nextHandler() {
        ThreadedHandler<T> handler = handlers.remove();
        handlers.add(handler);
        return handler;
    }
}
