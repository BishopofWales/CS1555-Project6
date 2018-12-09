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
		queryForCust(responseLine);
	}

	static void queryForCust(String login) {
		String sql = "select bidder,prod.auction_id,prod.status,name,prod.amount from Product prod left outer join BidLog bid on prod.auction_id = bid.auction_id and prod.amount = bid.amount where prod.seller = '"
				+ login + "'";
		ResultSet rs = MyAuction.query(sql);
		try {
			while (rs.next()) {
				System.out.println("-----------------");
				System.out.println("Auction ID: " + rs.getString("auction_id"));
				System.out.println("Name: " + rs.getString("name"));
				String status = rs.getString("status");
				String bidder = rs.getString("bidder");
				String amount = rs.getString("amount");

				System.out.println("Status: " + status);
				if (status.equals("sold")) {
					System.out.println("Buyer:" + bidder);
					System.out.println("Sold for: " + amount);
				} else if (bidder != null) {
					System.out.println("Highest bidder login: " + bidder);
					System.out.println("Highest bid: " + amount);
				}

			}
		} catch (Exception e) {
			System.out.println("Reading back search failed: " + e);
		}
	}

	static void allProds() {
		String sql = "select bidder,prod.auction_id,prod.status,name,prod.amount from Product prod left outer join BidLog bid on prod.auction_id = bid.auction_id and prod.amount = bid.amount";
		ResultSet rs = MyAuction.query(sql);
		try {
			while (rs.next()) {
				System.out.println("-----------------");
				System.out.println("Auction ID: " + rs.getString("auction_id"));
				System.out.println("Name: " + rs.getString("name"));
				String status = rs.getString("status");
				String bidder = rs.getString("bidder");
				String amount = rs.getString("amount");

				System.out.println("Status: " + status);
				if (status.equals("sold")) {
					System.out.println("Buyer:" + bidder);
					System.out.println("Sold for: " + amount);
				} else if (bidder != null) {
					System.out.println("Highest bidder login: " + bidder);
					System.out.println("Highest bid: " + amount);
				}

			}
		} catch (Exception e) {
			System.out.println("Reading back search failed: " + e);
		}
		// System.out.println("Here are statistics for customer.");

	}
}