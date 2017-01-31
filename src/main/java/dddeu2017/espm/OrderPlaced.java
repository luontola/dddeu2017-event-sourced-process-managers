package dddeu2017.espm;

import dddeu2017.espm.framework.Expirable;
import dddeu2017.espm.framework.MessageBase;

import java.time.Instant;

public class OrderPlaced extends MessageBase implements Expirable {

    public final Order order;

    public OrderPlaced(Order order) {
        this.order = order;
    }

    @Override
    public Instant expires() {
        return order.expires;
    }
}
