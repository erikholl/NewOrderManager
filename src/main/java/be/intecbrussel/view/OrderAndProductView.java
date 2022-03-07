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
        System.out.println(orderStatement(id));
    }

    private String orderStatement(int id) throws SQLException {
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

    public void updateOrder(String orderNr, boolean b) {
    }

    public void getLastOrder() throws SQLException {
        service.getLastOrder();
    }

    public void deleteLastOrder() {
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
