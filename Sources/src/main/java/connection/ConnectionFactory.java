package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    /**
     * Locatia bazei de date
     */
    private static final String DBURL = "jdbc:mysql://localhost:3306/warehousedb";
    /**
     * User-ul pentru logarea in baza de date
     */
    private static final String USER = "root";
    /**
     * Parola pentru logarea in baza de date
     */
    private static final String PASS = "cosmin";
    /**
     * Obiect nou de tipul ConnectionFactory
     */
    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda care creeaza o conexiune la baza de date.
     * @return conexiunea la baza de date
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     *
     * @return crearea conexiunii la baza de date
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Metoda care inchide conexiunea la baza de date.
     * @param connection conexiunea la baza de date
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
            }
        }
    }

    /**
     * Metoda care inchide un statement.
     * @param statement statement-ul care va fi inchis
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }

    /**
     * Metoda care inchide un rezultat.
     * @param resultSet rezultatul care va fi inchis
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }
}
