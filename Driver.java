import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Driver {
    static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
    static final String DB_PWD = "4031317";
    static final String DB_USR = "mph47";

    static Connection con = null;

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection(DB_URL, DB_USR, DB_PWD);
        } catch (Exception e) {
            System.out.println("Did not connect to database." + e);
        }
        Browsing.start(con, null);
        System.out.println(
                "Moving down the left side of the category hierarchy (ie. choosing the 0th subcategory each time).");
        ArrayList<String> cats = Browsing.getSubCategories(null);
        while (cats.size() > 0) {
            for (String cat : cats) {
                System.out.println(cat);
            }
            System.out.println("----------------------------");
            cats = Browsing.getSubCategories(cats.get(0));

        }
        System.out.println("Listing all products in alphabetical order.");
        Browsing.prodsByAlpha();
        System.out.println("Listing all products in order of price.");
        Browsing.prodsByPrice();
        System.out.println("Searching for keywords 'good' and 'not'");
        Searching.start(con, null);
        String[] keywords = { "not", "good" };
        Searching.executeSearch(keywords);

        System.out.println("2(C)");
        ProductStats.start(con, null);
        MyAuction.start();
        System.out.println("Listing admin information on all products");
        ProductStats.allProds();
        System.out.println("Listing admin information on products for bad68");
        ProductStats.queryForCust("bad68");

    }
}