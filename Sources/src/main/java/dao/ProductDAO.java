package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;
import model.Product;

public class ProductDAO {

    protected static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    /**
     * comanda care selecteaza toate campurile din tabela products, filtrate dupa coloana name
     */
    private final static String findStatementString = "SELECT * FROM product where name = ?";
    /**
     * comanda care insereaza un nou camp in tabela products
     */
    private static final String insertStatementString = "INSERT INTO product (name,qty,price)" + " VALUES (?,?,?)";
    /**
     * comanda care sterge un anumit camp din tabela products
     */
    private final static String deleteStatementString = "DELETE FROM product WHERE name = ?;";
    /**
     * comanda care face update coloanei qty pentru un anumit camp din tabela product
     */
    private final static String updateStatementString = "UPDATE product SET qty = ? WHERE name = ?";

    /**
     * Metoda care realizeaza cautarea in tabela product.
     * @param name numele produsului
     * @return obiectul de tipul Product gasit sau null in caz de esec
     */
    public static Product findByName(String name) {
        Product toReturn = null;

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        try {
            findStatement = dbConnection.prepareStatement(findStatementString);
            findStatement.setString(1, name);

            rs = findStatement.executeQuery();
            if (rs.next()) {

                int qty = rs.getInt("qty");
                double price = rs.getDouble("price");
                toReturn = new Product(name, qty, price);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"ProductDAO:findByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(findStatement);
            ConnectionFactory.close(dbConnection);
        }
        return toReturn;
    }

    /**
     * Metoda care realizeaza inserarea sau update-ul in tabela product.
     * @param product obiect de tipul Product
     * @return ID-ul produsului adaugat sau caruia i s-a facut update sau -1 in caz de esec
     */
    public static int insert(Product product) {
        Connection dbConnection = ConnectionFactory.getConnection();

        PreparedStatement insertStatement = null;
        int insertedId = -1;
        try {
            if (findByName(product.getName()) != null) {
                int qty = findByName(product.getName()).getQty() + product.getQty();
                insertStatement = dbConnection.prepareStatement(updateStatementString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setInt(1, qty);
                insertStatement.setString(2, product.getName());
            }
            else {
                insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, product.getName());
                insertStatement.setInt(2, product.getQty());
                insertStatement.setDouble(3, product.getPrice());
            }
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedId;
    }

    /**
     * Metoda care realizeaza stergerea din tabela client.
     * @param product numele produsului
     * @return ID-ul produsului sters sau -1 in caz de esec
     */
    public static int delete(String product) {
        Connection dbConnection = ConnectionFactory.getConnection();

        PreparedStatement deleteStatement = null;
        int deletedId = -1;
        try {
            deleteStatement = dbConnection.prepareStatement(deleteStatementString, Statement.RETURN_GENERATED_KEYS);
            deleteStatement.setString(1, product);
            deleteStatement.executeUpdate();

            ResultSet rs = deleteStatement.getGeneratedKeys();
            if (rs.next()) {
                deletedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(deleteStatement);
            ConnectionFactory.close(dbConnection);
        }
        return deletedId;
    }

}