import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Auction {
	static Connection con = null;
	static Scanner userIn;
	
	public static void start(Connection rCon, Scanner rUserIn) {
		con = rCon;
		userIn = rUserIn;
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
	
	public static String getUserInput(String prompt) {
		System.out.println(prompt + ": ");
		return userIn.nextLine().trim();
	}
	
	public static void auction() {
		String name, description, category, user, numDays, minPrice, username;
		ArrayList<String> params = new ArrayList<String>();
		
		//Get data from the user and add it to the parameter list
		username = getUserInput("Enter your username");
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
	
	public static ResultSet auctionQuery(ArrayList<String> params) {
		return query("Call proc_putProduct (?,?,?,?,?,?)", params);
	}
}