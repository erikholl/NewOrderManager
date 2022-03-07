package be.intecbrussel.view;

import be.intecbrussel.entities.Product;
import be.intecbrussel.service.OrderAndProductService;

import java.sql.SQLException;
import java.util.List;

public class OrderAndProductView {
    private OrderAndProductService service;

    public OrderAndProductView(OrderAndProductService service) {
        this.service = service;
    }

    // read order
    public void getOrder(int id) throws SQLException {
        if (service.getOrder(id).isEmpty()) {
            System.out.println("No order found with order id " + id);
        } else {
            System.out.println(service.getOrder(id).toString());
        }
    }

    public void getOrderInclProduct(int id) throws SQLException {
        System.out.println(orderDetails(id));
    }

    private String orderDetails(int id) throws SQLException {
        if (service.getOrder(id).isEmpty()) {
            return "No order found with order id " + id;
        } else {
            String order = service.getOrder(id).toString();
            List<Product> product = service.getProduct(id);
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (Product p : product) {
                sb.append("product line ").append(i).append(p.toString());
                sb.append("\n");
                i++;
            }
            String productDetail = sb.toString();
            return("Order details with orderId " + id + ":\n"
                    + order + "\n"
                    + productDetail);
        }
    }

    public void ordersNotSent() throws SQLException {
        service.ordersNotSent();
    }

    // update order not sent to sent
    public void updateOrderNotSentToSent(int id) throws SQLException {
        System.out.println("Update not sent to sent.");
        System.out.println("Order details prior to update:");
        getOrder(id);                           // print prior to update
        service.updateOrderNotSentToSent(id);   // update
        System.out.println("Order details after update:");
        getOrder(id);                           // print after update
    }

    public void getLastOrder() throws SQLException {
        System.out.println("Order details last order:");
        service.getLastOrder();
    }

    public void deleteLastOrder() throws SQLException {
        System.out.println("Order details last order: ");
        service.deleteLastOrder();
    }

    public void addOrder(String clientName,
                         String address, int postal, String city,
                         boolean vatFree, boolean isSent)
            throws SQLException {
        service.addOrder(clientName, address, postal, city, vatFree, isSent);
    }

    public void addProductAndOrder(String client, String address, int postal, String city,
                                   boolean vatFree, boolean isSent, String productName,
                                   int amount, double pricePerUnit) throws SQLException {
        service.addProductAndOrder(client, address, postal, city, vatFree,
                                      isSent, productName, amount,
                                      pricePerUnit);
    }

    public void addProductToOrder(int orderId, String productName,
                                   int amount, double pricePerUnit) throws SQLException {
        service.addProduct(orderId, productName, amount, pricePerUnit);
    }


}
