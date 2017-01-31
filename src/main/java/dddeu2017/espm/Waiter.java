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
        Item item = new Item();
        item.item = "pancake";
        item.quantity = 2;

        Order order = new Order();
        order.orderId = UUID.randomUUID();
        order.items.add(item);
        order.tableNumber = ThreadLocalRandom.current().nextInt(1, 20);

        next.handle(order);
        return order.orderId;
    }
}
