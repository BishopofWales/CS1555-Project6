import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class MyAuction {
	// javac -classpath '.;.\ojdbc6.jar' MyAuction.java
	static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	static final String DB_PWD = "4031317";
	static final String DB_USR = "mph47";
	static Scanner userIn;
	static String query, username, password;
	static SimpleDateFormat dateFormat;

	static Connection con = null;

	public static void main(String[] args) throws Exception{
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
		
		System.out.println("(u)ser or (a)dmin?");
		String responseLine = userIn.nextLine();
		char responseLetter = responseLine.charAt(0);
		if (responseLetter == 'u') {
			custMenu();
		} else if (responseLetter == 'a') {
			adminMenu();
		} else {
			quitting();
		}
	}

	// Verify login credentials
	public boolean login(int type) {
		/*
		 * type 2 = user (login)
		 */
		try {
			System.out.println("PLEASE ENTER YOUR LOGIN CREDENTIALS");
			username = getUserInput("Username");
			password = getUserInput("Password");

			ResultSet resultSet;
			if (type == 2) {
				resultSet = query("SELECT LOGIN, PASSWORD FROM USER");
			} else {
				resultSet = query("SELECT LOGIN, PASSWORD FROM ADMINISTRATOR");
			}

			while (resultSet.next()) {
				if (username.equals(resultSet.getString(1)) && password.equals(resultSet.getString(2))) {
					return true; // username and password combo is correct
				}
			}
			return false;
		}

		catch (SQLException e) {
			System.out.println("ERROR RUNNING QUERIES: " + e.toString());
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////////////
	// ADMINISTRATOR MENU
	//////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	public static void adminMenu() throws Exception{
		System.out.println("Welcome Administrator! Would you like to:");
		while (true) {
			System.out.println(
					"----------------\n(a)Register A New Customer\n(b)Update The System Date\n(c)Generate Product Statistics\n(d)In-Depth Product Statistic\n(e)Quit");
			String responseLine = userIn.nextLine();
			if (responseLine.length() > 1) {
				System.out.println("Please specify the letter for the option you would like");
				continue;
			}
			char responseLetter = responseLine.charAt(0);
			switch (responseLetter) {
			case 'q':
				quitting();

			case 'a':
				registerCustomer();
				break;
			case 'b':
				// updateDate();
				break;
			case 'c':
				productStats();
				break;
			case 'd':
				// inDepthStats();
				break;
			case 'e':
				quitting();
			default:
				System.out.println("Please select options (a-d) or (q) to quit");
				break;
			}
		}
	}

	// Helpers
	public static String getUserInput(String prompt) {
		System.out.println(prompt + ": ");
		return userIn.nextLine().trim();
	}

	public static ResultSet query(String query) {
		try {
			Statement s = con.createStatement();
			return s.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("ERROR RUNNING DATABASE QUERY: " + e.toString());
			return null;
		}
	}

	public static ResultSet query(String query, List<String> parameters) {
		try {
			PreparedStatement pStatement = con.prepareStatement(query);
			for (int i = 1; i <= parameters.size(); i++) {
				pStatement.setString(i, parameters.get(i - 1));
			}
			return pStatement.executeQuery();
		} catch (SQLException e) {
			System.out.println("ERROR RUNNING QUERY: " + e.toString());
			return null;
		}
	}

	// Update System Date
	public static void updateDate() {

		String date;
		date = getUserInput("\n PLEASE SET THE DATE (PLEASE FOLLOW FORMAT DD-MM-YYYY/HH:MI:SS)");
		ResultSet resultSet;
		resultSet = query("update sys_time set my_time = to_date('" + date + "', 'dd-mm-yyyy/hh:mi:ssam'");
		if (resultSet == null) {
			System.out.println("PLEASE ENTER DATE IN CORRECT FORMAT");
		} else {
			System.out.println("UPDATE SUCCESSFUL");
		}

	}

	
	////////////////////////////////////////////////////////////////////////////////
	// CUSTOMER MENU
	//////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	public static void custMenu() throws Exception {
		System.out.println("Welcome Customer! Would you like to:");
		while (true) {
			System.out.println(
					"----------------\n(a)Browse\n(b)Search\n(c)Sell\n(d)Bid\n(e)Sell\n(f)Get suggestions\n(q)Quit");
			String responseLine = userIn.nextLine();
			if (responseLine.length() > 1) {
				System.out.println("Please specify the letter for the option you would like");
				continue;
			}
			char responseLetter = responseLine.charAt(0);
			switch (responseLetter) {
			case 'q':
				quitting();

			case 'a':
				Browsing.start(con,userIn);
				break;
			case 'b':
				Searching.start(con,userIn);
				break;
			case 'c':
				auction();
				break;
			case 'd':
				bidding();
				break;
			case 'e':
				selling();
				break;
			case 'f':
				suggestions();
				break;
			default:
				System.out.println("Please select options (a-f) or (q) to quit");
				break;
			}

		}

	}
	

	

	public static void selling() {
		System.out.println("Selling");
	}

	public static void suggestions() {
		System.out.println("Suggestions");
	}

	public static void quitting() {
		try {
			con.close();
		} catch (Exception e) {
			System.out.println("Could not close connection.");
		}
		System.exit(0);
	}

	

	//Method for putting an item up for auction
	public static void auction() throws Exception{
		String name, description, category, user, numDays, minPrice;
		ArrayList<String> params = new ArrayList<String>();
		
		//Get data from the user and add it to the parameter list
		name = getUserInput("Enter the name of your product");
		description = getUserInput("Enter a description for your product (optional)");
		category = getUserInput("Enter the category of your product");
		numDays = getUserInput("Enter the amount of days the auction will last");
		minPrice = getUserInput("Enter the minimum price you will accept");
		params.add(name);
		params.add(description);
		params.add(username);
		params.add(category);
		params.add(numDays);
		params.add(minPrice);
		
		ResultSet resultSet = null;
		
		try {
			//Get the results from the call of proc_putProduct
			resultSet = auctionQuery(params);
		} catch (Exception e) {
			System.out.println("Error putting up auction: " + e.toString());
		}

		if (resultSet == null) {
			System.out.println("Error starting auction");
		} else {
			System.out.println("\nAuction started successfully\n");
		}
	}
	
	//Call the query(String query, List<String> params) method with proc_putProduct
	public static ResultSet auctionQuery(ArrayList<String> params) {
		return query("Call proc_putProduct (?,?,?,?,?,?)", params);
	}
	
	//Method for bidding on an auction
	public static void bidding() throws Exception{
		String amount, auctionID;
		ArrayList<String> params = new ArrayList<String>();
		
		//Get data from the user and add it to the parameter list
		auctionID = getUserInput("Enter the auction ID to bid on");
		amount = getUserInput("Enter the amount you wish to bid");
		params.add(auctionID);
		params.add(username);
		params.add(amount);
		
		ResultSet resultSet = null;
		
		try {
			//Get the results from the insert on bidlog
			resultSet = biddingQuery(params);
		} catch (Exception e) {
			System.out.println("Error bidding: " + e.toString());
		}

		if (resultSet == null) {
			System.out.println("Error placing bid");
		} else {
			System.out.println("\nBid placed successfully\n");
		}
	}
	
	//Call the query(String query, List<String> params) method with an insert on bidlog
	public static ResultSet biddingQuery(ArrayList<String> params) {
		return query("Insert into Bidlog values (1,?,?,sysdate,?)", params);
	}
	
	//Method for registering a new customer
	public static void registerCustomer() throws Exception {
		String name, login, password, address, email, admin;
		ArrayList<String> params = new ArrayList<String>();
		
		//Get data from the user and add it to the parameter list
		name = getUserInput("Enter a name");
		login = getUserInput("Enter a login");
		password = getUserInput("Enter a password");
		address = getUserInput("Enter an address");
		email = getUserInput("Enter an email");
		admin = getUserInput("Is this user an administrator? y/n");
		params.add(name);
		params.add(login);
		params.add(password);
		params.add(address);
		params.add(email);
		
		
		ResultSet resultSet = null;
		
		try {
			//Get the results from the insert on customer or administrator
			resultSet = registerCustomerQuery(params, admin);
		} catch (Exception e) {
			System.out.println("Error registering customer: " + e.toString());
		}
				
		if (resultSet == null) {
			System.out.println("Error registering customer");
		} else {
			System.out.println("\nCustomer registered successfully\n");
		}
	}
	
	public static ResultSet registerCustomerQuery(ArrayList<String> params, String admin) {
		//Check if the customer will be an administrator or not
		if (admin.equalsIgnoreCase("y")) {
			//Call the query(String query, List<String> params) method with an insert on administrator
			return query("Insert into administrator values (?,?,?,?,?)", params);
		} else if (admin.equalsIgnoreCase("n")) {	
			//Call the query(String querym List<String> params) method with an insert on customer
			return query("Insert into customer values (?,?,?,?,?)", params);	
		} else {
			System.out.println("Invalid response, please answer 'y' or 'n'");
			return null;
		}
	}
	
	
	
	private static void productStats() {
		
	}
	
	private static void inDepthStats() {
		
	}
}
