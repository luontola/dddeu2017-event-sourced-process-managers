package dddeu2017.espm.framework;

public interface Publisher {

    <T extends MessageBase> void publish(T message);
}
