package dddeu2017.espm.framework;

import dddeu2017.espm.Order;
import dddeu2017.espm.OrderHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Repeater implements OrderHandler {

    private final List<OrderHandler> handlers;

    public Repeater(OrderHandler... handlers) {
        this.handlers = new ArrayList<>(Arrays.asList(handlers));
    }

    @Override
    public void handle(Order order) {
        for (OrderHandler handler : handlers) {
            handler.handle(order);
        }
    }
}
