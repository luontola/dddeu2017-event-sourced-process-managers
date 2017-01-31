package dddeu2017.espm;

import dddeu2017.espm.framework.MessageBase;

public class OrderPlaced extends MessageBase {

    public final Order order;

    public OrderPlaced(Order order) {
        this.order = order;
    }
}
