package dddeu2017.espm.framework;

public interface Handler<T> {

    void handle(T message);
}
