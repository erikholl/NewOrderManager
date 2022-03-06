package be.intecbrussel.entities;

import be.intecbrussel.service.OrderAndProductService;

import java.sql.Date;

public class Order {
    public static int count; // public due to access in service, not used though

    // DB variables
    private int orderId;
    private String orderNr;
    private String client;
    private String address;
    private int postal;
    private String city;
    private boolean vatFree;
    private boolean isSent;
    private Date orderDate;

    // constructor for input; orderNr and orderDate to be generated, then
    // goes to main Order constructor
    public Order(String client, String address, int postal,
                 String city, boolean vatFree, boolean isSent) {
        this(OrderAndProductService.generateOrderNr(), client, address,
             postal, city, vatFree, isSent, OrderAndProductService.getOrderDate());
    }

    // constructor to add order to DB
    public Order(String orderNr, String client, String address, int postal,
                 String city, boolean vatFree, boolean isSent, Date orderDate) {
        this.orderNr = orderNr;
        this.client = client;
        this.address = address;
        this.postal = postal;
        this.city = city;
        this.vatFree = vatFree;
        this.isSent = isSent;
        this.orderDate = orderDate;
    }

    // constructor incl order-id, needed for getting queries from DB
    public Order(int orderId, String orderNr, String client, String address,
                 int postal, String city, boolean vatFree,
                 boolean isSent, Date orderDate) {
        this.orderId = orderId;
        this.orderNr = orderNr;
        this.client = client;
        this.address = address;
        this.postal = postal;
        this.city = city;
        this.vatFree = vatFree;
        this.isSent = isSent;
        this.orderDate = orderDate;
    }

    public String getOrderNr() {
        return orderNr;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
//                "orderId=" + getOrderId() +
                "orderNr='" + orderNr + '\'' +
                ", client='" + client + '\'' +
                ", address='" + address + '\'' +
                ", postal=" + postal +
                ", city='" + city + '\'' +
                ", vatFree=" + vatFree +
                ", isSent=" + isSent +
                ", orderDate=" + orderDate +
                '}';
    }
}
