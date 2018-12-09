import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Benchmark {
    static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
    static final String DB_PWD = "258852bd"; // 4031317
    static final String DB_USR = "bad68"; // mph47
    static Connection con = null;
    static final int RPT_CNT = 100;
    static Scanner userIn;
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));
        userIn = new Scanner(System.in);

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection(DB_URL, DB_USR, DB_PWD);
        } catch (Exception e) {
            System.out.println("Did not connect to database." + e);
        } 
        MyAuction.start();
        testBrowsing();
        testSearching();
        testProductStats();
        testRegisterCustomer();
        testAuction();
        testBidding();
        
		try {
			con.close();
		} catch (Exception e) {
			System.out.println("Could not close connection.");
		}
        
    }

    public static void testBrowsing() {
        Browsing.start(con, null);
        System.out.println("Moving down the left side of the category hierarchy 100 times");

        ArrayList<String> cats = Browsing.getSubCategories(null);
        for (int i = 0; i < 100; i++) {
            while (cats.size() > 0) {
                cats = Browsing.getSubCategories(cats.get(0));
            }
        }

        System.out.println("Listing all products in alphabetical order.");
        Browsing.prodsByAlpha();
        System.out.println("Listing all products in order of price.");
        Browsing.prodsByPrice();
    }

    public static void testSearching() {
        System.out.println("Searching for keywords 'good' and 'not' 100 times");
        Searching.start(con, null);
        String[] keywords = { "not", "good" };
        for (int i = 0; i < 100; i++) {
            Searching.executeSearch(keywords);
        }
    }

    public static void testProductStats() {
        ProductStats.start(con, null);
        System.out.println("Listing admin information on all products");
        ProductStats.allProds();
        System.out.println("Listing admin information on products for bad68");
        ProductStats.queryForCust("bad68");
    }

    public static void testBidding() {
        Bidding.start(con, userIn);

        ArrayList<String> params = new ArrayList<String>();

        // Bid on an auction
        System.out.println("Testing bidding on an auction");
        params.add("1");
        params.add("testcust");
        params.add("100000");

        ResultSet results = null;

        try {
            results = Bidding.biddingQuery(params);
        } catch (Exception e) {
            System.out.println("Error in biddingQuery: " + e.toString());
        }

        if (results == null) {
            System.out.println("Error in biddingQuery");
        } else {
            System.out.println("Bid placed successfully");
        }

        
    }

    public static void testAuction() {
        Auction.start(con, userIn);

        ArrayList<String> params = new ArrayList<String>();

        // Add an auction to the database
        System.out.println("Testing adding an auction to the database 100 times.");
        for(int i = 0; i < RPT_CNT;i++){
            String iter = Integer.toString(i);
            params.clear();
            params.add("Couch" + iter);
            params.add("No holes!");
            params.add("testcust");
            params.add("Furniture");
            params.add("10");
            params.add("600");

            ResultSet results = null;

            try {
                results = Auction.auctionQuery(params);
            } catch (Exception e) {
                System.out.println("Error in auctionQuery: " + e.toString());
            }

            if (results == null) {
                System.out.println("Error in auctionQuery");
            } else {
                System.out.println("Auction created successfully");
            }
        }
       
    }

    public static void testRegisterCustomer() {
        RegisterCustomer.start(con, userIn);
        ResultSet results = null;
        
        ArrayList<String> params = new ArrayList<String>();
        // Register a customer
        System.out.println("Testing registering a customer to the database 100 times.");
        for(int i = 0; i < RPT_CNT; i++){ 
            String iter = Integer.toString(i);
            String admin = "n";
            params.clear();
            params.add("testCust"+iter);
            params.add("12345"+iter);
            params.add("adam"+iter);
            params.add("210 S Bouquet St"+iter);
            params.add(iter+"@gmail.com");


            try {
                results = RegisterCustomer.registerCustomerQuery(params, admin);
            } catch (Exception e) {
                System.out.println("Error in registerCustomerQuery: " + e.toString());
            }

            if (results == null) {
                System.out.println("Error in registerCustomerQuery");
            } else {
                System.out.println("Customer registered successfully");
            }

        }
        // Register an admin
        System.out.println("Testing registering an admin to the database 100 times");
        for(int i = 0; i < RPT_CNT; i++){
            String iter = Integer.toString(i);
            params.clear();
            String admin = "y";
            results = null;

            params.add("admin" + iter);
            params.add("123456" + iter);
            params.add("bob" + iter);
            params.add("212 S Bouquet St"+iter);
            params.add(iter+"@gmail.com");

            try {
                results = RegisterCustomer.registerCustomerQuery(params, admin);
            } catch (Exception e) {
                System.out.println("Error in registerCustomerQuery: " + e.toString());
            }

            if (results == null) {
                System.out.println("Error in registerCustomerQuery");
            } else {
                System.out.println("Admin registered successfully");
            }

        }
       
    }
}