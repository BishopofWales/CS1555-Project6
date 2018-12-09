import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Driver {
	static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	static final String DB_PWD = "258852bd"; //4031317
	static final String DB_USR = "bad68"; //mph47
	static Scanner userIn;
	static Connection con = null;
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.class.path"));
		userIn = new Scanner(System.in);

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection(DB_URL, DB_USR, DB_PWD);
		} catch (Exception e) {
			System.out.println("Did not connect to database." + e);
		} finally {

		}
		
		testRegisterCustomer();
		testAuction();
		testBidding();
	}
	
	public static void testBidding() {
		Bidding.start(con, userIn);
		
		ArrayList<String> params = new ArrayList<String>();
		
		//Bid on an auction
		System.out.println("Testing bidding on an auction");
		params.add("1");
		params.add("adam");
		params.add("100000");
		
		ResultSet results = null;
		
		try {
			results = Bidding.biddingQuery(params);
		} catch (Exception e) {
			System.out.println("Error in biddingQuery: " + e.toString());
		}
		
		if(results == null) {
			System.out.println("Error in biddingQuery");
		} else {
			System.out.println("Bid placed successfully");
		}
		
		//Bid on an auction with a low amount
		System.out.println("Testing bidding on an auction, but the amount bid is too low");
		params.clear();
		results = null;
		
		params.add("1");
		params.add("adam");
		params.add("1");
		
		try {
			results = Bidding.biddingQuery(params);
		} catch (Exception e) {
			System.out.println("Error in biddingQuery: " + e.toString());
		}
		
		if(results == null) {
			System.out.println("Error in biddingQuery");
		} else {
			System.out.println("Bid placed successfully");
		}
	}
	
	public static void testAuction() {
		Auction.start(con, userIn);
		
		ArrayList<String> params = new ArrayList<String>();
		
		//Add an auction to the database
		System.out.println("Testing adding an auction to the database");
		params.add("Couch");
		params.add("No holes!");
		params.add("adam");
		params.add("Furniture");
		params.add("10");
		params.add("600");
		
		ResultSet results = null;
		
		try {
			results = Auction.auctionQuery(params);
		} catch (Exception e) {
			System.out.println("Error in auctionQuery: " + e.toString());
		}
		
		if(results == null) {
			System.out.println("Error in auctionQuery");
		} else {
			System.out.println("Auction created successfully");
		}
		
		//Add an auction without a leaf node category
		System.out.println("Testing adding an auction to the database, but the category is not a leaf node");
		params.clear();
		results = null;
		
		params.add("LG G4");
		params.add("Worst phone ever");
		params.add("adam");
		params.add("Electronics");
		params.add("10");
		params.add("600");
		
		try {
			results = Auction.auctionQuery(params);
		} catch (Exception e) {
			System.out.println("Error in auctionQuery: " + e.toString());
		}
		
		if(results == null) {
			System.out.println("Error in auctionQuery");
		} else {
			System.out.println("Auction created successfully");
		}
	}
	
	public static void testRegisterCustomer() {
		RegisterCustomer.start(con, userIn);
		
		ArrayList<String> params = new ArrayList<String>();
		String admin = "n";
		//Register a customer
		System.out.println("Testing registering a customer to the database");
		params.add("testCust");
		params.add("12345");
		params.add("adam");
		params.add("210 S Bouquet St");
		params.add("something@gmail.com");
		
		ResultSet results = null;
		
		try {
			results = RegisterCustomer.registerCustomerQuery(params, admin);
		} catch (Exception e) {
			System.out.println("Error in registerCustomerQuery: " + e.toString());
		}
		
		if(results == null) {
			System.out.println("Error in registerCustomerQuery");
		} else {
			System.out.println("Customer registered successfully");
		}
		
		//Attempt to register a customer with a conflicting login
		System.out.println("Testing registering a customer to the database, but the login is already taken");
		params.clear();
		admin = "n";
		results = null;
		
		
		params.add("testCust");
		params.add("123457");
		params.add("charlie");
		params.add("214 S Bouquet St");
		params.add("test@gmail.com");
		
		try {
			results = RegisterCustomer.registerCustomerQuery(params, admin);
		} catch (Exception e) {
			System.out.println("Error in registerCustomerQuery: " + e.toString());
		}
		
		if(results == null) {
			System.out.println("Error in registerCustomerQuery");
		} else {
			System.out.println("Customer registered successfully");
		}
		
		//Register an admin
		System.out.println("Tesint registering an admin to the database");
		params.clear();
		admin = "y";
		results = null;
		
		
		params.add("testAdmin");
		params.add("123456");
		params.add("bob");
		params.add("212 S Bouquet St");
		params.add("example@gmail.com");
		
		try {
			results = RegisterCustomer.registerCustomerQuery(params, admin);
		} catch (Exception e) {
			System.out.println("Error in registerCustomerQuery: " + e.toString());
		}
		
		if(results == null) {
			System.out.println("Error in registerCustomerQuery");
		} else {
			System.out.println("Admin registered successfully");
		}
		
	}
}