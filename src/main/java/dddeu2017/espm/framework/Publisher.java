package dddeu2017.espm.framework;

public interface Publisher {

    <T> void publish(T message);
}
