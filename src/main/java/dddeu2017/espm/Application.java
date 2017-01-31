package dddeu2017.espm;

public class Application {
    public static void main(String[] args) {
        Order order = new Order();
        order.items.add(new Item());
        order.items.add(new Item());
        System.out.println(order);
    }
}
