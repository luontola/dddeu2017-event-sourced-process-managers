package dddeu2017.espm;

import dddeu2017.espm.framework.MessageBase;

public class OrderPaid extends MessageBase {

    public final Order order;

    public OrderPaid(Order order) {
        this.order = order;
    }
}
