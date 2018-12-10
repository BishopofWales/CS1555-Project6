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
	
	public static void bidding() {
		String amount, auctionID, username;
		ArrayList<String> params = new ArrayList<String>();
		
		//Get data from the user and add it to the parameter list
		username = MyAuction.getUserInput("Enter your username");
		auctionID = MyAuction.getUserInput("Enter the auction ID to bid on");
		amount = MyAuction.getUserInput("Enter the amount you wish to bid");
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
		
		try{
			resultSet.close();
		}
		catch(Exception e){
			System.out.println("Could not close result" + e);
		}

	}
	
	public static ResultSet biddingQuery(ArrayList<String> params) {
		return MyAuction.query("Insert into Bidlog values (1,?,?,sysdate,?)", params);
	}
}