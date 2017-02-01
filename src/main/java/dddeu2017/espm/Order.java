package dddeu2017.espm;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    public UUID orderId;
    public Instant expires;
    public int tableNumber;
    public List<Item> items = new ArrayList<>();
    public int subtotal;
    public int tax;
    public int total;
    public int cookTime;
    public String ingredients;
    public boolean paid;
    public boolean dodgy;

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, new MultilineRecursiveToStringStyle() {
            @Override
            protected boolean accept(Class<?> clazz) {
                return clazz == Order.class || clazz == Item.class;
            }
        }).toString();
    }
}
