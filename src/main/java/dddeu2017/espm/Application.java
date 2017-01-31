package dddeu2017.espm;

public class Application {
    public static void main(String[] args) {
        OrderPrinter printer = new OrderPrinter();
        Cook cook = new Cook(printer);
        Waiter waiter = new Waiter(cook);
        waiter.placeOrder();
    }
}
