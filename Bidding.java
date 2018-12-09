import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Bidding {
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
	
	public static void bidding() {
		String amount, auctionID, username;
		ArrayList<String> params = new ArrayList<String>();
		
		//Get data from the user and add it to the parameter list
		username = getUserInput("Enter your username");
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
	
	public static ResultSet biddingQuery(ArrayList<String> params) {
		return query("Insert into Bidlog values (1,?,?,sysdate,?)", params);
	}
}