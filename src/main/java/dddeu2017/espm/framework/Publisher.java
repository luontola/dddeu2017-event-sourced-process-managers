package dddeu2017.espm.framework;

import dddeu2017.espm.Order;

public interface Publisher {

    void publish(String topic, Order order);
}
