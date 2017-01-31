package dddeu2017.espm.framework;

import dddeu2017.espm.HandlerOrder;
import dddeu2017.espm.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadedHandler implements HandlerOrder {

    private static final Logger log = LoggerFactory.getLogger(ThreadedHandler.class);

    private final HandlerOrder handler;
    private final BlockingQueue<Order> queue = new ArrayBlockingQueue<>(100);

    public ThreadedHandler(HandlerOrder handler) {
        this.handler = handler;
    }

    @Override
    public void handle(Order order) {
        try {
            queue.put(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void start() {
        Thread t = new Thread(this::forwardToHandler);
        t.start();
    }

    private void forwardToHandler() {
        while (!Thread.interrupted()) {
            try {
                Order order = queue.take();
                handler.handle(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Throwable t) {
                log.error("Unhandled exception", t);
            }
        }
    }
}
