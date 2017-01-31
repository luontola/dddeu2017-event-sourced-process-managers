package dddeu2017.espm.framework;

import dddeu2017.espm.HandlerOrder;
import dddeu2017.espm.Order;
import dddeu2017.espm.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoreFair implements HandlerOrder {

    private final List<ThreadedHandler> handlers;

    public MoreFair(ThreadedHandler... handlers) {
        this.handlers = new ArrayList<>(Arrays.asList(handlers));
    }

    @Override
    public void handle(Order order) {
        while (true) {
            for (ThreadedHandler handler : handlers) {
                if (handler.getCount() < 5) {
                    handler.handle(order);
                    return;
                }
            }
            Util.sleep(1);
        }
    }
}
