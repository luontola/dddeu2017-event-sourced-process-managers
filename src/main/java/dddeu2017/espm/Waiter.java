package dddeu2017.espm;

import dddeu2017.espm.framework.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Waiter {

    private static final Logger log = LoggerFactory.getLogger(Waiter.class);

    private final Publisher publisher;

    public Waiter(Publisher publisher) {
        this.publisher = publisher;
    }

    public UUID placeOrder() {
        log.info("Placing an order");
        Order order = new Order();
        order.orderId = UUID.randomUUID();
        order.expires = Instant.now().plusSeconds(15);
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

        publisher.publish(OrderPlaced.class, order);
        return order.orderId;
    }
}
