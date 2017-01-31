package dddeu2017.espm.framework;

import java.util.ArrayList;
import java.util.List;

public class Repeater<T> implements Handler<T> {

    private final List<Handler<T>> handlers;

    public Repeater(List<Handler<T>> handlers) {
        this.handlers = new ArrayList<>(handlers);
    }

    @Override
    public void handle(T order) {
        for (Handler<T> handler : handlers) {
            handler.handle(order);
        }
    }
}
