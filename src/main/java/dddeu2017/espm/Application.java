package dddeu2017.espm;

public class Application {
    public static void main(String[] args) {
        OrderPrinter printer = new OrderPrinter();
        AssistantManager assistantManager = new AssistantManager(printer);
        Cook cook = new Cook(assistantManager);
        Waiter waiter = new Waiter(cook);
        waiter.placeOrder();
    }
}
