package be.intecbrussel.data;

import be.intecbrussel.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// extending OrderDAO to inherit OrderDAO properties & methods
public class ProductDAO extends OrderDAO {
    public ProductDAO(Connection connection) {
        super(connection);
    }

    // add order and product, new orderNr, new orderId
    public void addProductAndOrder(String client, String address, int postal, String city,
                                   boolean vatFree, boolean isSent, String productName,
                                   int amount, double pricePerUnit) throws SQLException {
        Product product = new Product(client, address, postal, city, vatFree,
                                      isSent, productName, amount,
                                      pricePerUnit);
        addOrder(client, address, postal, city, vatFree, isSent);
        String orderNr = product.getOrderNr();
        int orderId = getOrderId(orderNr);
        addProduct(orderId, productName, amount, pricePerUnit);
    }

    // add product belonging to existing orderId (input) to DB
    public void addProduct(int orderId, String productName, int amount,
                           double pricePerUnit) throws SQLException {
        Product product = new Product(orderId, productName, amount,
                                      pricePerUnit);
        String sqlAddProduct = "INSERT INTO order_products(order_id, " +
                "product_name, amount, price_per_unit) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement ps = getPS(sqlAddProduct);
        ps.setInt(1, orderId);
        ps.setString(2, productName);
        ps.setInt(3, amount);
        ps.setDouble(4, pricePerUnit);
        ps.executeUpdate();

        System.out.println("Product " + productName + " belonging to " +
                                   "orderId " + orderId + " added to the DB.");
    }

    // read product belonging to order with existing orderId (input)
    public List<Product> getProduct(int id) throws SQLException {
        String sqlGetProduct = "SELECT * " +
                "FROM order_products " +
                "INNER JOIN order_table ON order_products.order_id = order_table.id " +
                "WHERE order_id = " + id;
        ResultSet rs = getRS(sqlGetProduct);

        List<Product> productList = new ArrayList<>();
        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("order_id"),
                    rs.getString("product_name"),
                    rs.getInt("amount"),
                    rs.getDouble("price_per_unit")
            );
            productList.add(product);
        }
        return productList;
    }
}
