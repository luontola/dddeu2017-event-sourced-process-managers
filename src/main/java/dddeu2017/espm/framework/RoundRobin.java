package dddeu2017.espm.framework;

import dddeu2017.espm.Order;
import dddeu2017.espm.OrderHandler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class RoundRobin implements OrderHandler {

    private final Queue<OrderHandler> handlers;

    public RoundRobin(OrderHandler... handlers) {
        this.handlers = new LinkedList<>(Arrays.asList(handlers));
    }

    @Override
    public void handle(Order order) {
        handlers.peek().handle(order);
        handlers.add(handlers.remove());
    }
}
