package dddeu2017.espm.framework;

import dddeu2017.espm.commands.PublishAt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlarmClock implements Handler<PublishAt> {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Publisher publisher;

    public AlarmClock(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void handle(PublishAt message) {
        scheduler.schedule(() -> publisher.publish(message.message),
                Instant.now().until(message.when, ChronoUnit.MILLIS),
                TimeUnit.MILLISECONDS);
    }
}
