package model;

/**
 * Fiecare obiect de tipul Product are un nume unic, o cantitate si un pret.
 */
public class Product {
    /**
     * numele produsului
     */
    private String name;
    /**
     * cantitatea de produse
     */
    private int qty;
    /**
     * pretul unui produs
     */
    private double price;

    /**
     * Creeaza un nou obiect de tipul Product.
     * @param name String care va fi asignat campului name
     * @param qty int care va fi asignat campului qty
     * @param price double care va fi asignat campului price
     */
    public Product(String name, int qty, double price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    /**
     *
     * @return numele produsului
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return pretul produsului
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return cantitatea de produse
     */
    public int getQty() {
        return qty;
    }

}
