package dddeu2017.espm.framework;

public class NullPublisher implements Publisher {

    @Override
    public <T extends MessageBase> void publish(T message) {
    }
}
