package model;

/**
 * Fiecare obiect de tipul OrderItem are un orderItemID unic, un nume de client, un nume de produs si un intreg cantitate.
 * Clasa OrderItem are o variabila de clasa noOrders.
 */
public class OrderItem {
    /**
     * numarul total de facturi
     */
    private static int noOrders = 0;
    /**
     * ID-ul facturii
     */
    private int orderItemID;
    /**
     * numele clientului pentru care se face factura
     */
    private String clientName;
    /**
     * numele produsului din factura
     */
    private String productName;
    /**
     * cantitatea de produse care au fost comandate
     */
    private int qty;

    /**
     * Creeaza un nou obiect de tipul OrderItem.
     * @param clientName String care va fi asignat campului clientName
     * @param productName String care va fi asignat campului productName
     * @param qty int care va fi asignat campului qty
     */
    public OrderItem(String clientName, String productName, int qty) {
        noOrders++;
        this.orderItemID = noOrders;
        this.clientName = clientName;
        this.productName = productName;
        this.qty = qty;
    }

    /**
     *
     * @return ID-ul facturii
     */
    public int getOrderItemID() {
        return orderItemID;
    }

    /**
     *
     * @return numele clientului
     */
    public String getClientName() {
        return clientName;
    }

    /**
     *
     * @return numele produsului
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @return cantitatea de produse
     */
    public int getQty() {
        return qty;
    }

}
