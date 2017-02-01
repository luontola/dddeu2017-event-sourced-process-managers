package dddeu2017.espm;

import dddeu2017.espm.commands.CookFood;
import dddeu2017.espm.commands.PriceOrder;
import dddeu2017.espm.commands.PublishAt;
import dddeu2017.espm.events.OrderCooked;
import dddeu2017.espm.events.OrderPlaced;
import dddeu2017.espm.framework.MessageBase;
import dddeu2017.espm.framework.Publisher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dddeu2017.espm.util.Util.newUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;

public class EatFirstMidgetTest {

    private final FakePublisher publisher = new FakePublisher();
    private final Order order = new Order();
    private final UUID correlationId = newUUID();

    @Test
    public void when_food_is_cooked_in_time() {
        EatFirstMidget midget = new EatFirstMidget();
        midget.setPublisher(publisher);
        midget.handle(new OrderPlaced(order, correlationId, newUUID()));
        MessageBase timeout = publisher.getTimeoutMessage();
        publisher.clear();

        midget.handle(new OrderCooked(order, correlationId, newUUID()));
        midget.handle(timeout);

        assertThat(publisher.published, contains(instanceOf(PriceOrder.class)));
    }

    @Test
    public void when_food_is_not_cooked_in_time() {
        EatFirstMidget midget = new EatFirstMidget();
        midget.setPublisher(publisher);
        midget.handle(new OrderPlaced(order, correlationId, newUUID()));
        MessageBase timeout = publisher.getTimeoutMessage();
        publisher.clear();

        midget.handle(timeout);

        assertThat(publisher.published, contains(instanceOf(CookFood.class), instanceOf(PublishAt.class)));
    }

    private static class FakePublisher implements Publisher {
        public final List<MessageBase> published = new ArrayList<>();

        @Override
        public <T extends MessageBase> void publish(T message) {
            published.add(message);
        }

        public void clear() {
            published.clear();
        }

        public MessageBase getTimeoutMessage() {
            return get(PublishAt.class).message;
        }

        public <T> T get(Class<T> type) {
            for (MessageBase message : published) {
                if (message.getClass() == type) {
                    return type.cast(message);
                }
            }
            throw new AssertionError("No message of type " + type.getSimpleName() + " was published: " + published);
        }
    }
}