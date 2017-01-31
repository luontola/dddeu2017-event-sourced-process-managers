package dddeu2017.espm;

public interface Handler<T> {

    void handle(T message);
}
