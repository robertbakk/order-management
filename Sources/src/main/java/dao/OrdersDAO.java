package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;
import model.Order;

/**
 * Clasa comunica cu tabela orders din baza de date.
 */
public class OrdersDAO {

    protected static final Logger LOGGER = Logger.getLogger(OrdersDAO.class.getName());
    /**
     * comanda care selecteaza toate campurile din tabela orders, filtrate dupa coloana client
     */
    private final static String findStatementString = "SELECT * FROM orders where client = ?";
    /**
     * comanda care insereaza un nou camp in tabela orders
     */
    private static final String insertStatementString = "INSERT INTO orders (orderID,client,pret)" + " VALUES (?,?,?)";
    /**
     * comanda care face update coloanei pret pentru un anumit camp din tabela orders
     */
    private final static String updateStatementString = "UPDATE orders SET pret = ? WHERE client = ?";

    /**
     * Metoda care realizeaza cautarea in tabela orders.
     * @param clientName numele clientului
     * @return obiectul de tipul Order gasit sau null in caz de esec
     */
    public static Order findByClientName(String clientName) {
        Order toReturn = null;

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        try {
            findStatement = dbConnection.prepareStatement(findStatementString);
            findStatement.setString(1, clientName);
            rs = findStatement.executeQuery();
            if (rs.next()) {
                String client = rs.getString("client");
                double pret = rs.getDouble("pret");
                toReturn = new Order(client, pret);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"OrderDAO:findByClientName " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    /**
     * Metoda care realizeaza inserarea sau update-ul in tabela orders.
     * @param order obiect de tipul Order
     * @return ID-ul order-ului adaugat sau caruia i s-a facut update sau -1 in caz de esec
     */
    public static int insert(Order order) {
        Connection dbConnection = ConnectionFactory.getConnection();

        PreparedStatement insertStatement = null;
        int insertedId = -1;
        try {
            if (findByClientName(order.getClient()) != null) {
                double pret = findByClientName(order.getClient()).getPret() + order.getPret();
                insertStatement = dbConnection.prepareStatement(updateStatementString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setDouble(1, pret);
                insertStatement.setString(2, order.getClient());
            }
            else {
                insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setInt(1, order.getOrderID());
                insertStatement.setString(2, order.getClient());
                insertStatement.setDouble(3, order.getPret());
            }
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrderDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedId;
    }

}