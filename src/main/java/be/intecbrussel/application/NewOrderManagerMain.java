package be.intecbrussel.application;

import be.intecbrussel.data.Connectivity;
import be.intecbrussel.data.OrderDAO;
import be.intecbrussel.data.ProductDAO;
import be.intecbrussel.service.OrderAndProductService;
import be.intecbrussel.view.OrderAndProductView;

import java.sql.Connection;
import java.sql.SQLException;

// TODO: delete last order
// TODO: scanner for user / pw
// TODO: price total per order
// TODO: check month comparison current vs. last order
// TODO: check duplicate orderNr (exception class?)
// TODO: check orderNr generate
// TODO: order toString looks ugly
// TODO: apply ADD / SHOW / UPDATE sequence in all classes
// TODO: place sqlStrings together (if possible)
// TODO: have all systemOuts in view

public class NewOrderManagerMain {

    public static void main(String[] args) {

        try {
            // establish connection
            Connection connection = connect();
            OrderAndProductView view = createUserViewer(connection);
            System.out.println("Connection opened..");

            System.out.println("************** ADD TO DATABASE **************");
            // add orders - 2 ways:
            // 1 - addProductAndOrder - adds an Order with 1 product line
            // 2a - addOrder - adds an order, outputs orderId
            // 2b - addProduct to an order based on orderId
            view.addOrder("client", "street", 1111,
                          "city", false, true);
            view.addProductAndOrder("klant3000", "kerkstraat", 1000,
                                    "amsterdam", true,false,
                                    "Quadigy", 2, 259.6);
            view.addProductToOrder(1, "product", 2,
                                    329);
            view.addProductToOrder(6, "Maths", 2, 249);
            view.addProductToOrder(6, "Euclidean", 1, 199);
            view.addProductToOrder(6, "Batumi", 1, 139);
            view.addProductToOrder(6, "Quintet", 1, 299);
            view.addProductToOrder(6, "Sarajewo", 2, 69);

            System.out.println("************ SHOW FROM DATABASE *************");
            // show order details based on orderId
            view.getOrderInclProduct(3); // parameter orderId

            // show list of orders not sent (no product details)
            view.ordersNotSent();

            // show last order details
            view.getLastOrder();

            System.out.println("************ UPDATE IN DATABASE *************");
            // update an order not sent to sent (pick 1 from notSent list)
            view.updateOrderNotSentToSent(4);

            // delete last order
            // TODO
//            view.deleteLastOrder();

            connection.close();
            System.out.println("Connection closed.");

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
