package be.intecbrussel.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectivity {
    private static Connectivity instance = new Connectivity();
    private Connection connection;

    private Connectivity() {
        // from outside non-accessible no-arg constructor
    }

    public static Connectivity getInstance() {
        return instance;
    }

    public void initialise() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://moktok.intecbrussel.org:33061/erikh",
                "erikh",
                "erikh2021"
        );
    }

    // TODO: find a way to have the line 'connection ..' included in this Class
//    public Connection connect() throws SQLException {
//        connection = Connectivity.getInstance().getConnection();
//        return connection;
//    }

    public Connection getConnection() {
        return this.connection;
    }
}
