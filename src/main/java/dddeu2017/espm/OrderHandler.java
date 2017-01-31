package dddeu2017.espm;

public interface OrderHandler extends Handler<Order> {

    void handle(Order order);
}
