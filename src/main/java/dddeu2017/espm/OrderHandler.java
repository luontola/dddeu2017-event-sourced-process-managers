package dddeu2017.espm;

@Deprecated
public interface OrderHandler extends Handler<Order> {

    void handle(Order order);
}
