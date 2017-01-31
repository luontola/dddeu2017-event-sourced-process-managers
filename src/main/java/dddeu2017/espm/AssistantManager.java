package dddeu2017.espm;

import dddeu2017.espm.framework.Publisher;
import dddeu2017.espm.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AssistantManager implements OrderHandler {

    private static final Logger log = LoggerFactory.getLogger(AssistantManager.class);

    private static final Map<String, Integer> pricesByItem = new HashMap<>();

    static {
        pricesByItem.put("pancake", 10);
        pricesByItem.put("ice cream", 6);
    }

    private final Publisher publisher;

    public AssistantManager(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(Order order) {
        log.info("Calculating the totals");
        for (Item item : order.items) {
            item.price = pricesByItem.get(item.item);
            order.subtotal += item.price * item.quantity;
        }
        order.tax = (int) (order.subtotal * 0.25);
        order.total = order.subtotal + order.tax;
        Util.sleep(500);
        publisher.publish(new OrderPriced(order));
    }
}
