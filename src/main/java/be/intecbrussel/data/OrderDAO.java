package be.intecbrussel.data;

import be.intecbrussel.entities.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO{
    Connection connection;

    // constructor
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    // general Statement / ResultSet method (protected for access OrderDAO)
    protected ResultSet getRS(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    // general PrepStatement method (protected for access OrderDAO)
    protected PreparedStatement getPS(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    // insert order
    public void addOrder(String clientName, String address, int postal,
                        String city, boolean vatFree, boolean isSent)
            throws SQLException {
        Order order = new Order(clientName, address, postal, city, vatFree,
                                isSent);
        String orderNr = order.getOrderNr();
        Date orderDate = order.getOrderDate();
        String sqlAddOrder = "INSERT INTO order_table(order_number, " +
                "order_client, order_delivery_address, " +
                "order_delivery_postalcode, order_delivery_city, is_vat_free," +
                " is_send, order_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = getPS(sqlAddOrder);
        ps.setString(1, orderNr);
        ps.setString(2, clientName);
        ps.setString(3, address);
        ps.setInt(4, postal);
        ps.setString(5, city);
        ps.setBoolean(6, vatFree);
        ps.setBoolean(7, isSent);
        ps.setDate(8, orderDate);
        ps.executeUpdate();

        System.out.println("Order added to the DB. OrderID: " + getOrderId(orderNr));
    }

    protected int getOrderId(String orderNr) throws SQLException {
        String sqlGetOrderId = "SELECT id FROM order_table " +
                "WHERE order_number = '" + orderNr + "'";
        ResultSet getOrderId = getRS(sqlGetOrderId);
        int orderId = 0;
        while (getOrderId.next()) {
            int i = getOrderId.getInt("id");
            orderId = i;
        }
        return orderId;
    }

    // get order details for order id X
    public Optional<Order> getOrder(int id) throws SQLException {
        String sqlGetOrder = "SELECT * FROM order_table WHERE id = ?";
        PreparedStatement ps = getPS(sqlGetOrder);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Order order = new Order(
                    rs.getInt("id"),
                    rs.getString("order_number"),
                    rs.getString("order_client"),
                    rs.getString("order_delivery_address"),
                    rs.getInt("order_delivery_postalcode"),
                    rs.getString("order_delivery_city"),
                    rs.getBoolean("is_vat_free"),
                    rs.getBoolean("is_Send"),
                    rs.getDate("order_date")
            );
            return Optional.of(order);
        }
        return Optional.empty();
    }

    // get last order (no product details)
    public List<Order> getLastOrder() throws SQLException {
        List<Order> lastOrder = new ArrayList<>();
        String sqlLastOrder = "SELECT * " +
                "FROM order_table " +
                "ORDER BY order_date DESC, id DESC " +
                "LIMIT 1;";

        ResultSet rs = getRS(sqlLastOrder);
        while (rs.next()) {
            Order order = new Order(
                    rs.getString("order_number"),
                    rs.getString("order_client"),
                    rs.getString("order_delivery_address"),
                    rs.getInt("order_delivery_postalcode"),
                    rs.getString("order_delivery_city"),
                    rs.getBoolean("is_vat_free"),
                    rs.getBoolean("is_send"),
                    rs.getDate("order_date")
            );
            lastOrder.add(order);
        }
        return lastOrder;
    }

    // TODO: below is not yet used, didn't get month comparison working yet
    //  as part of orderNrGen
    // get month last order
    public int getMonthLastOrder() throws SQLException {
        LocalDate lastOrderDate = null;
        String sqlMonthLastOrder = "SELECT MAX(order_date) FROM order_table";

        ResultSet rs = getRS(sqlMonthLastOrder);
        while (rs.next()) {
            lastOrderDate = rs.getDate("order_date").toLocalDate();
        }
        return lastOrderDate.getMonthValue();
    }
}
