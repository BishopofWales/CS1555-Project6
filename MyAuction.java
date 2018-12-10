import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class MyAuction {
	// javac -classpath '.;.\ojdbc6.jar' MyAuction.java
	static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	static final String DB_PWD = "258852bd"; // 4031317
	static final String DB_USR = "bad68"; // mph47
	static Scanner userIn;
	static String query, username, password;
	static SimpleDateFormat dateFormat;
	static Statement mainStatement = null;
	static Connection con = null;

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.class.path"));

		start();
		LoginCredentials.run();
	}

	public static void start() {
		userIn = new Scanner(System.in);
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection(DB_URL, DB_USR, DB_PWD);
			mainStatement = con.createStatement();
		} catch (Exception e) {
			System.out.println("Did not connect to database." + e);
		}

		
	}

	////////////////////////////////////////////////////////////////////////////////
	// ADMINISTRATOR MENU
	//////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	public static void adminMenu() {
		System.out.println("Welcome Administrator! Would you like to:");
		while (true) {
			System.out.println(
					"----------------\n(a)Register A New Customer\n(b)Update The System Date\n(c)Generate Product Statistics\n(d)In-Depth Product Statistic\n(q)Quit");
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
				RegisterCustomer.start(con, userIn);
				RegisterCustomer.registerCustomer();
				break;
			case 'b':
				updateSystemTime.run();
				break;
			case 'c':
				ProductStats.start(con, userIn);
				ProductStats.productStats();
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
	public static void RefreshStatement(){
		try{
			mainStatement.close();
		}
		catch(Exception e){
			System.out.println("Could not close statement");
		}
		try{
			mainStatement = con.createStatement();
		}
		catch(Exception e){
			System.out.println("could not reopen statement");
		}
		
	}
	// Helpers
	public static boolean goodString(String input) {
		return input.matches("[a-zA-Z\\s]*$");
	}

	public static String getUserInput(String prompt) {
		System.out.println(prompt + ": ");
		return userIn.nextLine().trim();
	}

	public static ResultSet query(String query) {
		try {
			return mainStatement.executeQuery(query);
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

	// Register Customer
	public static void registerCustomer() {
		System.out.println("Registering Customer");
	}

	// In-Depth Product Stats
	public static void inDepthStats() {
		System.out.println("In Depth Stats");
	}
	////////////////////////////////////////////////////////////////////////////////
	// CUSTOMER MENU
	//////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	public static void custMenu() {
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
				Browsing.start(con, userIn);
				Browsing.browsing();
				break;
			case 'b':
				Searching.start(con, userIn);
				Searching.searching();
				break;
			case 'c':
				Auction.start(con, userIn);
				Auction.auction();
				break;
			case 'd':
				Bidding.start(con, userIn);
				Bidding.bidding();
				break;
			case 'e':
				selling();
				break;
			case 'f':
				productSuggestions.run();
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

}
