package dddeu2017.espm.framework;

import dddeu2017.espm.HandlerOrder;
import dddeu2017.espm.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Repeater implements HandlerOrder {

    private final List<HandlerOrder> handlers;

    public Repeater(HandlerOrder... handlers) {
        this.handlers = new ArrayList<>(Arrays.asList(handlers));
    }

    @Override
    public void handle(Order order) {
        for (HandlerOrder handler : handlers) {
            handler.handle(order);
        }
    }
}
