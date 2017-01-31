package dddeu2017.espm;

public class Cook implements HandlerOrder {

    private final HandlerOrder next;

    public Cook(HandlerOrder next) {
        this.next = next;
    }

    @Override
    public void handle(Order order) {
        order.ingredients = "spam";
        int cookTime = 2000;
        sleep(cookTime);
        order.cookTime = cookTime;
        next.handle(order);
    }

    private static void sleep(int cookTime) {
        try {
            Thread.sleep(cookTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
