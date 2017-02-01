package dddeu2017.espm;

import dddeu2017.espm.commands.CookFood;
import dddeu2017.espm.commands.PriceOrder;
import dddeu2017.espm.commands.PublishAt;
import dddeu2017.espm.commands.TakePayment;
import dddeu2017.espm.events.CookingTimedOut;
import dddeu2017.espm.events.DuplicateFoodCooked;
import dddeu2017.espm.events.GaveUpOnRetryingCooking;
import dddeu2017.espm.events.MidgetFinished;
import dddeu2017.espm.events.OrderCooked;
import dddeu2017.espm.events.OrderPaid;
import dddeu2017.espm.events.OrderPlaced;
import dddeu2017.espm.events.OrderPriced;
import dddeu2017.espm.framework.MessageBase;
import dddeu2017.espm.framework.Midget;
import dddeu2017.espm.framework.Publisher;

import java.time.Instant;

public class EatFirstMidget implements Midget {

    private Publisher publisher;
    private boolean cooked = false;

    @Override
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(MessageBase message) {
        if (message instanceof OrderPlaced) {
            handle((OrderPlaced) message);
        } else if (message instanceof CookingTimedOut) {
            handle((CookingTimedOut) message);
        } else if (message instanceof OrderCooked) {
            handle((OrderCooked) message);
        } else if (message instanceof OrderPriced) {
            handle((OrderPriced) message);
        } else if (message instanceof OrderPaid) {
            handle((OrderPaid) message);
        }
    }

    private void handle(OrderPlaced message) {
        publisher.publish(new CookFood(message.order, message.correlationId, message.id));
        publisher.publish(new PublishAt(Instant.now().plusSeconds(10),
                new CookingTimedOut(message.order, 1, message.correlationId, message.id),
                message.correlationId, message.id));
    }

    private void handle(CookingTimedOut message) {
        if (cooked) {
            return;
        }
        if (message.tries >= 3) {
            publisher.publish(new GaveUpOnRetryingCooking(message.order, message.correlationId, message.id));
            return;
        }
        publisher.publish(new CookFood(message.order, message.correlationId, message.id));
        publisher.publish(new PublishAt(Instant.now().plusSeconds(10),
                new CookingTimedOut(message.order, message.tries + 1, message.correlationId, message.id),
                message.correlationId, message.id));
    }

    private void handle(OrderCooked message) {
        if (cooked) {
            publisher.publish(new DuplicateFoodCooked(message.order, message.correlationId, message.id));
            return;
        }
        cooked = true;
        publisher.publish(new PriceOrder(message.order, message.correlationId, message.id));
    }

    private void handle(OrderPriced message) {
        publisher.publish(new TakePayment(message.order, message.correlationId, message.id));
    }

    private void handle(OrderPaid message) {
        publisher.publish(new MidgetFinished(message.correlationId, message.id));
    }
}
