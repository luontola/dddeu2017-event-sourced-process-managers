package dddeu2017.espm;

import dddeu2017.espm.framework.MessageBase;

public class OrderPriced extends MessageBase {

    public final Order order;

    public OrderPriced(Order order) {
        this.order = order;
    }
}
