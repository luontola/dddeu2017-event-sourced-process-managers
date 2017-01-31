package dddeu2017.espm.framework;

import dddeu2017.espm.HandlerOrder;
import dddeu2017.espm.Order;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin implements HandlerOrder {

    private final Queue<HandlerOrder> handlers;

    public RoundRobin(HandlerOrder... handlers) {
        this.handlers = new LinkedList<>(Arrays.asList(handlers));
    }

    @Override
    public void handle(Order order) {
        handlers.peek().handle(order);
        handlers.add(handlers.remove());
    }
}
