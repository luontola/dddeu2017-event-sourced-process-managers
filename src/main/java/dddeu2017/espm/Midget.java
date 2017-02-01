package dddeu2017.espm;

import dddeu2017.espm.commands.CookFood;
import dddeu2017.espm.commands.PriceOrder;
import dddeu2017.espm.commands.TakePayment;
import dddeu2017.espm.events.OrderCooked;
import dddeu2017.espm.events.OrderPlaced;
import dddeu2017.espm.events.OrderPriced;
import dddeu2017.espm.framework.Handler;
import dddeu2017.espm.framework.MessageBase;
import dddeu2017.espm.framework.Publisher;

public class Midget implements Handler<MessageBase> {

    private final Publisher publisher;

    public Midget(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(MessageBase message) {
        if (message instanceof OrderPlaced) {
            handle((OrderPlaced) message);
        } else if (message instanceof OrderCooked) {
            handle((OrderCooked) message);
        } else if (message instanceof OrderPriced) {
            handle((OrderPriced) message);
        }
    }

    private void handle(OrderPlaced message) {
        publisher.publish(new CookFood(message.order, message.correlationId, message.id));
    }

    private void handle(OrderCooked message) {
        publisher.publish(new PriceOrder(message.order, message.correlationId, message.id));
    }

    private void handle(OrderPriced message) {
        publisher.publish(new TakePayment(message.order, message.correlationId, message.id));
    }
}
