import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class ProductStats {
	static Connection con = null;
	static Scanner userIn;

	public static void start(Connection rCon, Scanner rUserIn) {
		userIn = rUserIn;
		con = rCon;
		productStats();
	}

	static void productStats() {

		System.out.println("Would you like stastics on a specific customer or all customers?");
		System.out.println("(a)Specific customer\n(b)All customers");
		String responseLine = userIn.nextLine();
		if (responseLine.length() > 1) {
			System.out.println("Please specify the letter for the option you would like.");
			productStats();
		}
		char responseLetter = responseLine.charAt(0);
		switch (responseLetter) {
		case 'c':
			return;
		case 'a':
			prodsByCust();
			break;
		case 'b':
			allProds();
			break;

		default:
			System.out.println("Please select (a) or (b), or (c) to return.");
			productStats();
			break;
		}
	}

	static void prodsByCust() {
		System.out.println("Please type the login of the user to list products.");
		String responseLine = userIn.nextLine();
		String sql = "select auction_id, name, status, bidder from Product where seller = '"
				+ MyAuction.filterString(responseLine) + "'";
		ResultSet rs = MyAuction.query(sql);
		try{
			while (rs.next()) {
				System.out.println("-----------------");
				System.out.println("Auction ID: " + rs.getString("auction_id"));
				System.out.println("Name: " + rs.getString("name"));	
				System.out.println("Status: " +  rs.getString("status"));
				System.out.println("Highest bidder: " + rs.getString("bidder"));
				
			}
		}
		catch(Exception e){
			System.out.println("Reading back search failed");
		}
		
	}

	static void allProds() {
		String sql ="select bidder,auction_id,bidsn from BidLog bid1  where amount = (select Max(amount) from bidlog bid2 group by auction_id having bid2.auction_id = bid1.auction_id )";
		ResultSet rs = MyAuction.query(sql);
		try{
			while (rs.next()) {
				System.out.println("-----------------");
				System.out.println("Auction ID: " + rs.getString("auction_id"));
				System.out.println("Highest bidder: " + rs.getString("bidder"));
			}
		}
		catch(Exception e){
			System.out.println("Reading back search failed");
		}
		// System.out.println("Here are statistics for customer.");

	}
}