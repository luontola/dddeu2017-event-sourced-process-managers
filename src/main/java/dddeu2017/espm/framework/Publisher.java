package dddeu2017.espm.framework;

import dddeu2017.espm.Order;

public interface Publisher {

    <T> void publish(Class<T> topic, Order order);
}
