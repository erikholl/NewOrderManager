package be.intecbrussel.service;

import be.intecbrussel.data.OrderDAO;
import be.intecbrussel.data.ProductDAO;
import be.intecbrussel.entities.Order;
import be.intecbrussel.entities.Product;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class OrderAndProductService {
    private OrderDAO orderDAO;
    private ProductDAO productDAO;

    // extra variables for process support
    private static LocalDate now = LocalDate.now();
    private static Date sqlDate = Date.valueOf(now);

    // constructor = dependency on Order- & ProductDAO
    public OrderAndProductService(OrderDAO orderDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
    }

    // static method for orderNr gen
    public static String generateOrderNr() {
        String ord = "ORD";
        DateTimeFormatter customF = DateTimeFormatter.ofPattern("yyyyMM");
        String date = customF.format(now);
        String seqNr = String.format("%04d", Order.count);
        Order.count++;
        return ord + "-" + date + "-" + seqNr;
    }

    // static method for sql Date, for orderDate
    public static Date getOrderDate() {
        return Date.valueOf(LocalDate.now());
    }

    public Optional<Order> getOrder(int id) throws SQLException {
        return this.orderDAO.getOrder(id);
    }

    public List<Product> getProduct(int id) throws SQLException {
        return this.productDAO.getProduct(id);
    }

    public void ordersNotSent() throws SQLException {
        List<Order> ordersNotSent = orderDAO.ordersNotSent();
        System.out.println("Orders not sent:");
        for (Order order : ordersNotSent) {
            System.out.println(order);
        }
    }

    public void updateOrderNotSentToSent(int id) throws SQLException {
        orderDAO.updateOrderNotSentToSent(id);
    }

    public void getLastOrder() throws SQLException {
        System.out.println(orderDAO.getLastOrder());
    }

    public void deleteLastOrder() throws SQLException {
        System.out.println(orderDAO.getLastOrder());
        orderDAO.deleteLastOrder();
    }

    public void addOrder(String clientName,
                           String address, int postal, String city,
                           boolean vatFree,
                           boolean isSent)
            throws SQLException {
        orderDAO.addOrder(clientName, address, postal, city, vatFree, isSent);
    }

    public void addProductAndOrder(String client, String address, int postal, String city,
                                   boolean vatFree, boolean isSent, String productName,
                                   int amount, double pricePerUnit) throws SQLException {
        productDAO.addProductAndOrder(client, address, postal, city, vatFree,
                                      isSent, productName, amount,
                                      pricePerUnit);
    }

    public void addProduct(int orderId, String productName,
                                   int amount, double pricePerUnit) throws SQLException {
        productDAO.addProduct(orderId, productName, amount, pricePerUnit);
    }


    // ********** BELOW METHODS ARE NOT USED - TOO COMPLEX FOR NOW TO
    // ********** IMPLEMENT THE LOGIC PERFORMING CHECKS ON MONTH DIFFERENCE
    // ********** AND DUPLICATE ORDERNR - YEAH, MOST LIKELY I'M MAKING IT TOO
    // ********** COMPLEX...
    // support methods to perform month comparison now vs last order
    public boolean isNewMonth() throws SQLException {
        int currentMonth = now.getMonthValue();
        int monthLastOrder = orderDAO.getMonthLastOrder();
        return currentMonth != monthLastOrder;
    }
    public void setOrderNrVars() throws SQLException {
        // month comparison now vs last order month - adapt count accordingly
        if (isNewMonth()) {
            Order.count = 1;
//            Order.substring = generateOrderNrSubstring();
        } else {
            Order.count++;
//            Order.substring = generateOrderNrSubstring();
        }
        // check DB on generated orderNr - if needed, generate new orderNr
    }

    // ***********************************************************************

}
