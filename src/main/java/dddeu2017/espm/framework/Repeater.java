package dddeu2017.espm.framework;

import dddeu2017.espm.HandlerOrder;
import dddeu2017.espm.Order;

import java.util.List;

public class Repeater implements HandlerOrder {

    private final List<HandlerOrder> handlers;

    public Repeater(List<HandlerOrder> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void handle(Order order) {
        for (HandlerOrder handler : handlers) {
            handler.handle(order);
        }
    }
}
