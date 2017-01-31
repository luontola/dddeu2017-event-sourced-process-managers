package dddeu2017.espm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Waiter {

    private static final Logger log = LoggerFactory.getLogger(Waiter.class);

    private final HandlerOrder next;

    public Waiter(HandlerOrder next) {
        this.next = next;
    }

    public UUID placeOrder() {
        log.info("Placing an order");
        Order order = new Order();
        order.orderId = UUID.randomUUID();
        order.tableNumber = ThreadLocalRandom.current().nextInt(1, 20);

        Item pancake = new Item();
        pancake.item = "pancake";
        pancake.quantity = 2;
        order.items.add(pancake);

        if (ThreadLocalRandom.current().nextBoolean()) {
            Item iceCream = new Item();
            iceCream.item = "ice cream";
            iceCream.quantity = 2;
            order.items.add(iceCream);
        }

        next.handle(order);
        return order.orderId;
    }
}
