package model;

/**
 * Fiecare obiect de tipul Client are un nume unic si o adresa.
 */
public class Client {
    /**
     * numele clientului
     */
    private String name;
    /**
     * adresa(orasul) clientului
     */
    private String address;

    /**
     * Creeaza un nou obiect de tipul Client.
     * @param name String care va fi asignat campului name
     * @param address String care va fi asignat campului address
     */
    public Client(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     *
     * @return numele clientului
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return adresa clientului
     */
    public String getAddress() {
        return address;
    }
}
