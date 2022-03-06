package be.intecbrussel.application;

import be.intecbrussel.data.Connectivity;
import be.intecbrussel.data.OrderDAO;
import be.intecbrussel.data.ProductDAO;
import be.intecbrussel.service.OrderAndProductService;
import be.intecbrussel.view.OrderAndProductView;

import java.sql.Connection;
import java.sql.SQLException;

// TODO: delete last order
// TODO: update sent status
// TODO: list orders not sent
// TODO: delete last order
// TODO: check month comparison current vs. last order
// TODO: check duplicate orderNr
// TODO: order toString looks ugly

public class NewOrderManagerMain {

    public static void main(String[] args) {

        try {
            // establish connection
            Connection connection = connect();
            OrderAndProductView view = createUserViewer(connection);
            System.out.println("connection opened..");

            // add orders - 2 ways:
            // 1 - addProductAndOrder - adds an Order with 1 product line
            // 2a - addOrder - adds an order, outputs orderId
            // 2b - addProduct to an order based on orderId


//            view.addOrder("client", "street", 1111,
//                          "city", false, true);
//            view.addProductAndOrder("klant3000", "kerkstraat", 1000,
//                                    "amsterdam", true,false,
//                                    "Quadigy", 2, 259.6);
//            view.addProductToOrder(1, "product", 2,
//                                    329);
            view.addProductToOrder(6, "Maths", 2, 249);
            view.addProductToOrder(6, "Euclidean", 1, 199);
            view.addProductToOrder(6, "Batumi", 1, 139);
            view.addProductToOrder(6, "Quintet", 1, 299);
            view.addProductToOrder(6, "Sarajewo", 2, 69);

            // read order details based on orderId
            view.getOrder(1); // parameter orderId

            // show list of orders not sent (not incl. product details)
//            view.ordersNotSent(false); // parameter is_sent boolean
            // update an order not sent to sent:
            // 1. pick an order not sent, show it (without product details)
            // 2. pick an order and update its sent status to sent
            // 3. show updated order
//            view.getOrder("ORD-202203-0001"); // parameter orderId
//            view.updateOrder("ORD-202203-0001", true);
//            view.getOrder("ORD-202203-0001");
            // delete last order
            // 1. collect last order (incl product details (not mentioned in A))
            // 2. delete last order (incl. product details)
            view.getLastOrder(); // no parameter - query needed to collect
            // last order based on date
//            view.deleteLastOrder(); // should not need parameter, last order
            // to get from getLastOrder and delete it

            // close connection

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection connect() throws SQLException {
        // establish connection
        Connectivity.getInstance().initialise();
        return Connectivity.getInstance().getConnection();
    }

    private static OrderAndProductView createUserViewer(Connection connection) {
        // create a view object
        return new OrderAndProductView(
                new OrderAndProductService(new OrderDAO(connection),
                                           new ProductDAO(
                                                   connection)));
    }
}
