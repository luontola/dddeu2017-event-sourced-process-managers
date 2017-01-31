package dddeu2017.espm.framework;

import dddeu2017.espm.HandlerOrder;
import dddeu2017.espm.Order;
import dddeu2017.espm.util.Util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class MoreFair implements HandlerOrder {

    private final Queue<ThreadedHandler> handlers;

    public MoreFair(ThreadedHandler... handlers) {
        this.handlers = new LinkedList<>(Arrays.asList(handlers));
    }

    @Override
    public void handle(Order order) {
        while (true) {
            ThreadedHandler handler = handlers.peek();
            handlers.add(handlers.remove());
            if (handler.getCount() < 5) {
                handler.handle(order);
                return;
            }
            Util.sleep(1);
        }
    }
}
