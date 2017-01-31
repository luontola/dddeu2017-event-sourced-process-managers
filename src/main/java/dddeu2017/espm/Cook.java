package dddeu2017.espm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Cook implements HandlerOrder {

    private static final Logger log = LoggerFactory.getLogger(Cook.class);

    private static final Map<String, String> ingredientsByItem = new HashMap<>();

    static {
        ingredientsByItem.put("pancake", "flour, milk");
        ingredientsByItem.put("ice cream", "cream, chocolate");
    }

    private final HandlerOrder next;

    public Cook(HandlerOrder next) {
        this.next = next;
    }

    @Override
    public void handle(Order order) {
        log.info("Making the food");
        for (Item item : order.items) {
            String ingredients = ingredientsByItem.get(item.item);
            addIngredients(order, ingredients);
        }
        int cookTime = 2000;
        Util.sleep(cookTime);
        order.cookTime += cookTime;
        next.handle(order);
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
