package presentation;

import java.io.FileOutputStream;
import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import connection.ConnectionFactory;
import dao.ClientDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import model.OrderItem;

/**
 * Clasa care se ocupa cu scrierea unor date in fisiere .pdf.
 */
public class Presentation {
    protected static final Logger LOGGERCLIENT = Logger.getLogger(ClientDAO.class.getName());
    protected static final Logger LOGGERPRODUCT = Logger.getLogger(ProductDAO.class.getName());
    protected static final Logger LOGGERORDER = Logger.getLogger(OrderItemDAO.class.getName());
    /**
     * comanda care selecteaza toate campurile din tabela client
     */
    private final static String selectAllClients = "SELECT * FROM client";
    /**
     * comanda care selecteaza toate campurile din tabela product
     */
    private final static String selectAllProducts = "SELECT * FROM product";
    /**
     * comanda care selecteaza toate campurile din tabela orders
     */
    private final static String selectAllOrders = "SELECT * FROM orders";
    private static int nrReportClient = 0;
    private static int nrReportProduct = 0;
    private static int nrReportOrder = 0;
    private static int nrBill = 0;

    /**
     * Metoda care face un report nou pentru tabela Client
     * @throws SQLException arunca exceptie de tipul SQLException
     */
    public void reportClient() throws SQLException {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement selectStatement = null;
        ResultSet rs = null;
        try {
            selectStatement = dbConnection.prepareStatement(selectAllClients);
            rs = selectStatement.executeQuery();
        } catch (SQLException e) {
            LOGGERCLIENT.log(Level.WARNING, e.getMessage());
        }
        Document pdf = new Document();
        try {
            nrReportClient++;
            PdfWriter.getInstance(pdf, new FileOutputStream("ReportClient" + nrReportClient + ".pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        pdf.open();
        PdfPTable tabel = tabelaClient(rs);
        try {
            pdf.add(tabel);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        ConnectionFactory.close(rs);
        ConnectionFactory.close(selectStatement);
        ConnectionFactory.close(dbConnection);
        pdf.close();
    }

    /**
     *  Metoda care face un tabel cu toti clientii, pe care il returneaza pentru a fi adaugat in pdf.
     * @param rs campurile tabelei client
     * @return tabel cu toti clientii
     * @throws SQLException arunca exceptie de tipul SQLException
     */
    public PdfPTable tabelaClient(ResultSet rs) throws SQLException {
        PdfPTable tabel = new PdfPTable(2);
        PdfPCell cell;

        tabel.addCell(new PdfPCell(new Phrase("Name")));
        tabel.addCell(new PdfPCell(new Phrase("Address")));

        while (rs.next()) {
            String name = rs.getString("name");
            cell = new PdfPCell(new Phrase(name));
            tabel.addCell(cell);
            String address = rs.getString("address");
            cell = new PdfPCell(new Phrase(address));
            tabel.addCell(cell);
        }
        return tabel;
    }
    /**
     * Metoda care face un report nou pentru tabela Product
     * @throws SQLException arunca exceptie de tipul SQLException
     */
    public void reportProduct() throws SQLException {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement selectStatement = null;
        ResultSet rs = null;
        try {
            selectStatement = dbConnection.prepareStatement(selectAllProducts);
            rs = selectStatement.executeQuery();
        } catch (SQLException e) {
            LOGGERPRODUCT.log(Level.WARNING, e.getMessage());
        }
        Document pdf = new Document();
        try {
            nrReportProduct++;
            PdfWriter.getInstance(pdf, new FileOutputStream("ReportProduct" + nrReportProduct + ".pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        pdf.open();
        PdfPTable tabel = tabelaProduct(rs);
        try {
            pdf.add(tabel);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        ConnectionFactory.close(rs);
        ConnectionFactory.close(selectStatement);
        ConnectionFactory.close(dbConnection);
        pdf.close();
    }

    /**
     *  Metoda care face un tabel cu toate produsele, pe care il returneaza pentru a fi adaugat in pdf.
     * @param rs campurile tabelei product
     * @return tabel cu toate produsele
     * @throws SQLException arunca exceptie de tipul SQLException
     */
    public PdfPTable tabelaProduct(ResultSet rs) throws SQLException {
        PdfPTable tabel = new PdfPTable(3);
        PdfPCell cell;

        tabel.addCell(new PdfPCell(new Phrase("Name")));
        tabel.addCell(new PdfPCell(new Phrase("Qty")));
        tabel.addCell(new PdfPCell(new Phrase("Price")));

        while (rs.next()) {
            String name = rs.getString("name");
            cell = new PdfPCell(new Phrase(name));
            tabel.addCell(cell);
            int qty = rs.getInt("qty");
            cell = new PdfPCell(new Phrase(qty + ""));
            tabel.addCell(cell);
            double price = rs.getDouble("price");
            cell = new PdfPCell(new Phrase(price + ""));
            tabel.addCell(cell);
        }
        return tabel;
    }

    /**
     * Metoda care face un report nou pentru tabela orderitem
     * @param orderItem obiect de tipul OrderItem
     */
    public void createBill(OrderItem orderItem) {
        Document pdf = new Document();
        try {
            nrBill++;
            PdfWriter.getInstance(pdf, new FileOutputStream("Bill" + nrBill + ".pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        pdf.open();
        PdfPTable tabel = tabelaBill(orderItem);
        try {
            pdf.add(tabel);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        pdf.close();
    }

    /**
     *  Metoda care face un tabel pentru o factura, pe care il returneaza pentru a fi adaugat in pdf.
     * @param orderItem obiect de tipul OrderItem
     * @return tabel cu datele facturii
     */
    public PdfPTable tabelaBill(OrderItem orderItem) {
        PdfPTable tabel = new PdfPTable(3);
        PdfPCell cell;

        tabel.addCell(new PdfPCell(new Phrase("Client name")));
        tabel.addCell(new PdfPCell(new Phrase("Product name")));
        tabel.addCell(new PdfPCell(new Phrase("Qty")));

        String clientName = orderItem.getClientName();
        cell = new PdfPCell(new Phrase(clientName));
        tabel.addCell(cell);
        String productName = orderItem.getProductName();
        cell = new PdfPCell(new Phrase(productName));
        tabel.addCell(cell);
        int qty = orderItem.getQty();
        cell = new PdfPCell(new Phrase(qty + ""));
        tabel.addCell(cell);
        return tabel;
    }

    /**
     * Metoda care face o factura noua, care corespunde unui camp al tabelei order
     * @throws SQLException arunca exceptie de tipul SQLException
     */
    public void reportOrder() throws SQLException {
        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement selectStatement = null;
        ResultSet rs = null;
        try {
            selectStatement = dbConnection.prepareStatement(selectAllOrders);
            rs = selectStatement.executeQuery();
        } catch (SQLException e) {
            LOGGERORDER.log(Level.WARNING, e.getMessage());
        }
        Document pdf = new Document();
        try {
            nrReportOrder++;
            PdfWriter.getInstance(pdf, new FileOutputStream("Order" + nrReportOrder + ".pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        pdf.open();
        PdfPTable tabel = tabelaOrder(rs);
        try {
            pdf.add(tabel);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        ConnectionFactory.close(rs);
        ConnectionFactory.close(selectStatement);
        ConnectionFactory.close(dbConnection);
        pdf.close();
    }

    /**
     *  Metoda care face un tabel cu toate comenzile, pe care il returneaza pentru a fi adaugat in pdf.
     * @param rs campurile tabelei order
     * @return tabel cu toate comenzile
     * @throws SQLException arunca exceptie de tipul SQLException
     */
    public PdfPTable tabelaOrder(ResultSet rs) throws SQLException {
        PdfPTable tabel = new PdfPTable(2);
        PdfPCell cell;

        tabel.addCell(new PdfPCell(new Phrase("Client name")));
        tabel.addCell(new PdfPCell(new Phrase("Price")));

        while (rs.next()) {
            String name = rs.getString("client");
            cell = new PdfPCell(new Phrase(name));
            tabel.addCell(cell);
            double price = rs.getDouble("pret");
            cell = new PdfPCell(new Phrase(price + ""));
            tabel.addCell(cell);
        }
        return tabel;
    }

    /**
    *  Metoda care face un report pentru a afisa un mesaj atunci cand nu se poate crea o factura pentru ca nu sunt suficiente produse de un anumit tip.
    * @param orderItem obiect de tipul OrderItem
    */
    public void underStock(OrderItem orderItem) {
        Document pdf = new Document();
        try {
            nrBill++;
            PdfWriter.getInstance(pdf, new FileOutputStream("Bill" + nrBill + ".pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        pdf.open();
        String s = "Client " + orderItem.getClientName() + " placed an order of " + orderItem.getQty() + " products of type " + orderItem.getProductName() + ".\n";
        s += "There are only " + ProductDAO.findByName(orderItem.getProductName()).getQty() + " products of type " + orderItem.getProductName() + " in stock.\n";
        try {
            pdf.add(new Chunk(""));
            pdf.add(new Paragraph(s));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        pdf.close();
    }

}
