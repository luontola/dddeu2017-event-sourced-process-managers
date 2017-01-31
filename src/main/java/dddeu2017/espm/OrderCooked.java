package dddeu2017.espm;

import dddeu2017.espm.framework.MessageBase;

public class OrderCooked extends MessageBase {

    public final Order order;

    public OrderCooked(Order order) {
        this.order = order;
    }
}
