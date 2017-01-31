package dddeu2017.espm;

public class Application {
    public static void main(String[] args) {
        OrderPrinter printer = new OrderPrinter();
        Cashier cashier = new Cashier(printer);
        AssistantManager assistantManager = new AssistantManager(cashier);
        Cook cook = new Cook(assistantManager);
        Waiter waiter = new Waiter(cook);
        waiter.placeOrder();
    }
}
