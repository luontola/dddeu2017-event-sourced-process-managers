package dddeu2017.espm;

public class Application {
    public static void main(String[] args) {
        OrderPrinter printer = new OrderPrinter();
        Waiter waiter = new Waiter(printer);
        waiter.placeOrder();
    }
}
