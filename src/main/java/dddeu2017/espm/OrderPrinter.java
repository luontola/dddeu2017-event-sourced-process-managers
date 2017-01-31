package dddeu2017.espm;

public class OrderPrinter implements HandlerOrder {
    @Override
    public void handle(Order order) {
        System.out.println(order);
    }
}
