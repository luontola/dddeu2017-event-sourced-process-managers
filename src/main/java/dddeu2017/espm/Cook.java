package dddeu2017.espm;

import dddeu2017.espm.framework.Publisher;
import dddeu2017.espm.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Cook implements OrderHandler {

    private static final Logger log = LoggerFactory.getLogger(Cook.class);

    private static final Map<String, String> ingredientsByItem = new HashMap<>();

    static {
        ingredientsByItem.put("pancake", "flour, milk, egg");
        ingredientsByItem.put("ice cream", "ice cream, chocolate");
    }

    private final String name;
    private final int cookTime;
    private final Publisher publisher;

    public Cook(String name, int cookTime, Publisher publisher) {
        this.name = name;
        this.cookTime = cookTime;
        this.publisher = publisher;
    }

    @Override
    public void handle(Order order) {
        log.info("{} is making the food", name);
        for (Item item : order.items) {
            String ingredients = ingredientsByItem.get(item.item);
            addIngredients(order, ingredients);
        }
        Util.sleep(cookTime);
        order.cookTime += cookTime;
        publisher.publish(OrderCooked.class, order, new OrderCooked(order));
    }

    private void addIngredients(Order order, String ingredients) {
        if (order.ingredients == null) {
            order.ingredients = "";
        } else {
            order.ingredients += ", ";
        }
        order.ingredients += ingredients;
    }
}
