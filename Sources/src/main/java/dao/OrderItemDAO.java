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
import model.OrderItem;
import model.Product;
import presentation.Presentation;

/**
 * Clasa comunica cu tabela orderitem din baza de date.
 */
public class OrderItemDAO {

    protected static final Logger LOGGER = Logger.getLogger(OrderItemDAO.class.getName());
    /**
     * comanda care insereaza un nou camp in tabela orderitem
     */
    private static final String insertStatementString = "INSERT INTO orderitem (orderItemID,clientName,productName,qty)" + " VALUES (?,?,?,?)";

    /**
     * Metoda care realizeaza inserarea in tabela orderitem.
     * @param orderItem obiect de tipul OrderItem
     * @return ID-ul orderItem-ului adaugat sau -1 in caz de esec
     */
    public static int insert(OrderItem orderItem) {
        Connection dbConnection = ConnectionFactory.getConnection();

        PreparedStatement insertStatement = null;
        int insertedId = -1;
        try {
            Presentation pr = new Presentation();
            insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1, orderItem.getOrderItemID());
            insertStatement.setString(2, orderItem.getClientName());
            insertStatement.setString(3, orderItem.getProductName());
            insertStatement.setInt(4, orderItem.getQty());
            if (orderItem.getQty()<=ProductDAO.findByName(orderItem.getProductName()).getQty()) {
                insertStatement.executeUpdate();
                Product p = new Product(orderItem.getProductName(), -orderItem.getQty(), ProductDAO.findByName(orderItem.getProductName()).getPrice());
                ProductDAO.insert(p);
                pr.createBill(orderItem);
                double price = 0;
                price = orderItem.getQty()*ProductDAO.findByName(orderItem.getProductName()).getPrice();
                Order or  = new Order (orderItem.getClientName(), price);
                OrdersDAO.insert(or);
            }
            else {
                pr.underStock(orderItem);
            }
            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrderItemDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedId;
    }


}