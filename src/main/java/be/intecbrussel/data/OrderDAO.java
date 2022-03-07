package be.intecbrussel.data;

import be.intecbrussel.entities.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO{
    Connection connection;

    // sqlStrings (when they don't have a local variable embedded
    String sqlAddOrder = "INSERT INTO order_table(order_number, " +
            "order_client, order_delivery_address, " +
            "order_delivery_postalcode, order_delivery_city, is_vat_free," +
            " is_send, order_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    String sqlGetOrder = "SELECT * FROM order_table WHERE id = ?";
    String sqlOrdersNotSent = "SELECT * FROM order_table WHERE is_send = 0;";
    String sqlUpdateNotSentToSent = "UPDATE order_table SET is_send = 1 WHERE id = ?";
    String sqlLastOrder = "SELECT * FROM order_table ORDER BY order_date DESC, id DESC LIMIT 1;";
    String sqlMonthLastOrder = "SELECT MAX(order_date) FROM order_table";

    // constructor
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    // insert order
    public void addOrder(String clientName, String address, int postal,
                        String city, boolean vatFree, boolean isSent)
            throws SQLException {
        Order order = new Order(clientName, address, postal, city, vatFree,
                                isSent);
        String orderNr = order.getOrderNr();
        Date orderDate = order.getOrderDate();
//
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

    // query for orderId - used as part of add order and product in 1 method,
    // where orderNr is generated, then used to collect orderId from DB,
    // which is then used to add (1) product line to the DB
    protected int getOrderId(String orderNr) throws SQLException {
        String sqlGetOrderId = "SELECT id FROM order_table " +
                "WHERE order_number = '" + orderNr + "'";
        ResultSet getOrderId = getRS(sqlGetOrderId);
        getOrderId.next();
        return getOrderId.getInt("id");
    }

    // query order details for order id X
    public Optional<Order> getOrder(int id) throws SQLException {
        PreparedStatement ps = getPS(sqlGetOrder);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return collectOrderFromDB(rs);
    }

    // query list of orders not sent
    public List<Order> ordersNotSent() throws SQLException {
        List<Order> ordersNotSent;
        ResultSet rs = getRS(sqlOrdersNotSent);
        ordersNotSent = createListOrdersFromDB(rs);
        return ordersNotSent;
    }

    // update DB: order not sent to sent
    public void updateOrderNotSentToSent(int id) throws SQLException {
        PreparedStatement ps = getPS(sqlUpdateNotSentToSent);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // query last order (no product details)
    public Optional<Order> getLastOrder() throws SQLException {
        ResultSet rs = getRS(sqlLastOrder);
        return collectOrderFromDB(rs);
    }

    // delete last order
    public void deleteLastOrder() throws SQLException {
        ResultSet rs = getRS(sqlLastOrder);
        rs.next();
        int lastOrderId = rs.getInt("id");
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM order_table WHERE id = " + lastOrderId);
        statement.executeUpdate("DELETE FROM order_products WHERE order_id = " + lastOrderId);
    }

    // ******************** HELP METHODS **********************
    // general Statement / ResultSet method (protected for access OrderDAO)
    protected ResultSet getRS(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    // general PrepStatement method (protected for access OrderDAO)
    protected PreparedStatement getPS(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    // help method to query DB. potentially get multiple orders back
    // and build a list from these
    private List<Order> createListOrdersFromDB(ResultSet rs) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        while (rs.next()) {
            Order order = new Order(
                    rs.getInt("id"),
                    rs.getString("order_number"),
                    rs.getString("order_client"),
                    rs.getString("order_delivery_address"),
                    rs.getInt("order_delivery_postalcode"),
                    rs.getString("order_delivery_city"),
                    rs.getBoolean("is_vat_free"),
                    rs.getBoolean("is_send"),
                    rs.getDate("order_date")
            );
            orderList.add(order);
        }
        return orderList;
    }

    // help method to query DB and get 1 optional Order back
    private Optional<Order> collectOrderFromDB(ResultSet rs) throws SQLException {
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

    // TODO: below is not yet used, didn't get month comparison working yet
    //  as part of orderNrGen
    // get month last order
    public int getMonthLastOrder() throws SQLException {
        LocalDate lastOrderDate = null;
        ResultSet rs = getRS(sqlMonthLastOrder);
        while (rs.next()) {
            lastOrderDate = rs.getDate("order_date").toLocalDate();
        }
        return lastOrderDate.getMonthValue();
    }
}
