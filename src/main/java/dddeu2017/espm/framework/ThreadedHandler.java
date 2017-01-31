package dddeu2017.espm.framework;

import dddeu2017.espm.Order;
import dddeu2017.espm.OrderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadedHandler implements OrderHandler, Runnable {

    private static final Logger log = LoggerFactory.getLogger(ThreadedHandler.class);

    private final String name;
    private final OrderHandler handler;
    private final BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    public ThreadedHandler(String name, OrderHandler handler) {
        this.name = name;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return queue.size();
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
        Thread t = new Thread(this, name);
        t.start();
    }

    @Override
    public void run() {
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
