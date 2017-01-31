package dddeu2017.espm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssistantManager implements HandlerOrder {

    private static final Logger log = LoggerFactory.getLogger(AssistantManager.class);

    private final HandlerOrder next;

    public AssistantManager(HandlerOrder next) {
        this.next = next;
    }

    @Override
    public void handle(Order order) {
        log.info("Calculating the totals");
        for (Item item : order.items) {
            order.subtotal += item.price * item.quantity;
        }
        order.tax = (int) (order.subtotal * 0.25);
        order.total = order.subtotal + order.tax;
        Util.sleep(500);
        next.handle(order);
    }
}
