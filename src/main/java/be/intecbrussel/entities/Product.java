package be.intecbrussel.entities;

public class Product {
    private int orderId;
    private String orderNr;
    private String productName;
    private int amount;
    private double pricePerUnit;

    // constructor for order + product at once - ordernr will be generated
    // via order constructor
    public Product(String client, String address, int postal, String city,
                   boolean vatFree, boolean isSent, String productName,
                   int amount, double pricePerUnit) {
        this.productName = productName;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        Order order = new Order(client, address, postal, city, vatFree, isSent);
        this.orderNr = order.getOrderNr();
    }

    // constructor only used when orderId is input
    public Product(int orderId, String productName, int amount,
                   double pricePerUnit) {
        this.orderId = orderId;
        this.productName = productName;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public String getOrderNr() {
        return orderNr;
    }

    @Override
    public String toString() {
        return "Product{" +
                "orderId=" + orderId +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
