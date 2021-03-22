package presentation;

import dao.ClientDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import model.Client;
import model.OrderItem;
import model.Product;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

/**
 * Clasa care preia continutul fisierului.
 */
public class Parser {
    /**
     * Citeste fiecare linie a fisierului
     * @param in numele fisierului de intrare
     */
    public void readFile(String in) {
        File fisier = new File(in);
        Scanner myReader = null;
        try {
            myReader = new Scanner(fisier);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            try {
                parseCommand(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica ce fel de comanda este specificata pe fiecare linie.
     * @param data linia din fisier
     * @throws SQLException arunca exceptie de tipul SQLException
     */
    public void parseCommand(String data) throws SQLException {
        Presentation p = new Presentation();
        if (data.equals("Report client"))
            p.reportClient();
        else if (data.equals("Report product"))
            p.reportProduct();
        else if (data.equals("Report order"))
            p.reportOrder();
        else {
            String[] s = data.split(": ");
            if (s[0].equals("Insert client"))
                insertClient(s[1]);
            else if (s[0].equals("Insert product"))
                insertProduct(s[1]);
            else if (s[0].equals("Order"))
                insertOrder(s[1]);
            else if (s[0].equals("Delete client"))
                deleteClient(s[1]);
            else if (s[0].equals("Delete product"))
                deleteProduct(s[1]);
        }
    }


    /**
     * Se creeaza un nou obiect de tip Client, care este inserat apoi in baza de date.
     * @param client String cu clientul care trebuie inserat
     */
    public void insertClient(String client) {
        String[] s = client.split(", ");
        Client c  = new Client (s[0],s[1]);
        ClientDAO.insert(c);
    }

    /**
     * Se creeaza un nou obiect de tip Product, care este inserat apoi in baza de date.
     * @param product String cu produsul care trebuie inserat
     */
    public void insertProduct(String product) {
        String[] s = product.split(", ");
        Product p  = new Product (s[0], Integer.parseInt(s[1]), Double.parseDouble(s[2]));
        ProductDAO.insert(p);
    }

    /**
     * Se creeaza un nou obiect de tip OrderItem, care este inserat apoi in baza de date.
     * @param order String cu order-ul care trebuie inserat
     */
    public void insertOrder(String order) {
        String[] s = order.split(", ");
        OrderItem o  = new OrderItem (s[0], s[1], Integer.parseInt(s[2]));
        OrderItemDAO.insert(o);
    }

    /**
     * Se sterge un obiect de tipul Client din baza de date.
     * @param client String cu clientul care trebuie sters
     */
    public void deleteClient(String client) {
        String[] s = client.split(", ");
        Client c  = new Client (s[0],s[1]);
        ClientDAO.delete(c);
    }

    /**
     * Se sterge un obiect de tipul Product din baza de date.
     * @param product String cu produsul care trebuie sters
     */
    public void deleteProduct(String product) {
        ProductDAO.delete(product);
    }

}
