package dddeu2017.espm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    public int tableNumber;
    public List<Item> items = new ArrayList<>();
    public int subtotal;
    public int tax;
    public int total;
    public int cookTime;
    public String ingredients;
    public UUID orderId;
}
