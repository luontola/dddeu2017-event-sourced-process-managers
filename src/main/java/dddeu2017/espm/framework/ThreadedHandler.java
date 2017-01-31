package dddeu2017.espm.framework;

import dddeu2017.espm.HandlerOrder;
import dddeu2017.espm.Order;
import dddeu2017.espm.util.Util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadedHandler implements HandlerOrder {

    private final HandlerOrder handler;
    private final Queue<Order> queue = new ConcurrentLinkedQueue<>();

    public ThreadedHandler(HandlerOrder handler) {
        this.handler = handler;
    }

    @Override
    public void handle(Order order) {
        queue.add(order);
    }

    public void start() {
        Thread t = new Thread(this::forwardToHandler);
        t.start();
    }

    private void forwardToHandler() {
        while (!Thread.interrupted()) {
            Order order = queue.poll();
            if (order != null) {
                handler.handle(order);
            } else {
                Util.sleep(1);
            }
        }
    }
}
