package be.intecbrussel.application;

import be.intecbrussel.data.Connectivity;
import be.intecbrussel.data.OrderDAO;
import be.intecbrussel.data.ProductDAO;
import be.intecbrussel.service.OrderAndProductService;
import be.intecbrussel.view.OrderAndProductView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

// TODO: delete last order
// TODO: check orderNr generate
// TODO: price total per order
// TODO: check month comparison current vs. last order
// TODO: check duplicate orderNr (exception class?)
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
            view.addOrder("Stoner", "MillHill 3", 808, "Skifcruh", true,
                    false);
            view.addProductToOrder(9, "DFAM", 1, 519);
            view.addProductToOrder(9, "minimixer", 2, 89);
            view.addProductToOrder(9, "multiplexico", 5, 11);
            view.addProductToOrder(9, "stackables", 24, 4.99);

            view.addOrder("Bolle", "In De Steeg", 0,
                          "OnsDorp", true, false);
            view.addProductAndOrder("HAL9000", "andromeda", 100000000,
                                    "Kratshask", false,true,
                                    "MoonRev", 1, 89);
            view.addProductToOrder(1, "ChurchVerb", 1,
                                    365);
            view.addProductToOrder(6, "Sienergie", 1, 649);
            view.addProductToOrder(6, "Tetra", 1, 171);
            view.addProductToOrder(6, "Lyvv'nrar", 1, 600);
            view.addProductToOrder(6, "Quintet", 1, 299);
            view.addProductToOrder(6, "Prog", 1, 59);

            System.out.println("************ SHOW FROM DATABASE *************");
            // show order details based on orderId
            view.getOrderInclProduct(8); // parameter orderId
//
//            // show list of orders not sent (no product details)
            view.ordersNotSent();
//
//            // show last order details
            view.getLastOrder();

            System.out.println("************ UPDATE IN DATABASE *************");
            // update an order not sent to sent (pick 1 from notSent list)
            view.updateOrderNotSentToSent(7);

//            // delete last order
            view.deleteLastOrder();

            connection.close();
            System.out.println("Connection closed.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection connect() throws SQLException {
        // establish connection
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter username");
        String userName = scanner.nextLine();
        System.out.println("enter password");
        String pwd = scanner.nextLine();
        System.out.println("enter name database");
        String dbName = scanner.nextLine();

        Connectivity.getInstance().initialise(userName, pwd, dbName);
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
