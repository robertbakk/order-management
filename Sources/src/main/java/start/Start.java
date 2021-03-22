package start;

import presentation.Parser;

/**
 * Clasa main a proiectului.
 */
public class Start {
    /**
     * Creeaza un nou obiect de tip Parser si apeleaza metoda readFile din clasa Parser.
     * @param args pastreaza numele fisierului de intrare
     */
    public static void main(String[] args) {
        Parser p = new Parser();
        p.readFile(args[0]);
    }
}
