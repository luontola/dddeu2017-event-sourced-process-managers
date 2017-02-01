package dddeu2017.espm.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadedHandler<T> implements Handler<T>, Runnable {

    private static final Logger log = LoggerFactory.getLogger(ThreadedHandler.class);

    private final String name;
    private final Handler<T> handler;
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    public ThreadedHandler(String name, Handler<T> handler) {
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
    public void handle(T message) {
        try {
            queue.put(message);
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
                T message = queue.take();
                log.trace("Handle message {}", message);
                handler.handle(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Throwable t) {
                log.error("Unhandled exception", t);
            }
        }
    }
}
