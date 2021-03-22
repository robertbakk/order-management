package model;

/**
 * Fiecare obiect de tipul Oorder are un orderID unic, un nume de client si un pret.
 * Clasa Order are o variabila de clasa noOrders.
 */
public class Order {
    /**
     * ID-ul comenzii
     */
    private int orderID;
    /**
     * numarul total de comenzi
     */
    private static int noOrders = 0;
    /**
     * numele clientului care a dat comanda
     */
    private String client;
    /**
     * suma pe care o are de platit clientul
     */
    private double pret;

    /**
     * Creeaza un nou obiect de tipul Order.
     * @param client String care va fi asignat campului client
     * @param pret double care va fi asignat campului pret
     */
    public Order(String client, double pret) {
        noOrders++;
        this.orderID = noOrders;
        this.client = client;
        this.pret = pret;
    }

    /**
     *
     * @return ID-ul comenzii
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     *
     * @return numele clientului
     */
    public String getClient() {
        return client;
    }

    /**
     *
     * @return pretul total
     */
    public double getPret() {
        return pret;
    }

}
