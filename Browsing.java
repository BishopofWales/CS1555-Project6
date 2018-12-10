import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Browsing {
	static Connection con = null;
	static Scanner userIn;

	public static void start(Connection rCon, Scanner rUserIn) {
		userIn = rUserIn;
		con = rCon;
	}

	public static void browsing() {
		System.out.println(
				"----------------\n(a)List products by price\n(b)List products alphabetically\n(c)Browse products by category\n(d)Return to main customer menu");
		String responseLine = userIn.nextLine();
		if (responseLine.length() > 1) {
			System.out.println("Please specify the letter for the option you would like");
			browsing();
		}
		char responseLetter = responseLine.charAt(0);
		switch (responseLetter) {
		case 'd':
			return;
		case 'a':
			prodsByPrice();
			break;
		case 'b':
			prodsByAlpha();
			break;
		case 'c':
			browsingByCat();
			break;
		default:
			System.out.println("Please select options (a-c) or (d) to return");
			browsing();
			break;
		}
	}

	public static void browsingByCat() {
		// TO DO: add sort by price, add order alphabetically
		try {
			System.out.println("Here are the root categories, select a number to choose a category.");
			ArrayList<String> categories = getSubCategories(null);
			chooseSub(categories);

		} catch (Exception e) {
			System.out.print("Browsing query failed" + e);
		}
		// attributes, e.g, auction id, name, description (if through search as
		// mentioned in task
		// (b)), highest bid amount

	}

	public static void chooseSub(ArrayList<String> categories) {
		System.out.println("Please choose a subcategory.");
		displayCategories(categories);
		String responseLine = userIn.nextLine();

		if (isNumeric(responseLine)) {

			int catIndex = Integer.parseInt(responseLine);
			if (catIndex >= categories.size()) {
				System.out.println("Invalid category number");
				browsing();
			} else {
				String selCat = categories.get(catIndex);
				ArrayList<String> selCatSubs = getSubCategories(selCat);
				if (selCatSubs.size() == 0) {
					listProds(selCat);
				} else {
					chooseSub(selCatSubs);
				}
			}

		} else {
			System.out.println("A non-number was entered, returning to main menu");
			browsing();
		}

	}

	static void listProds(String category) {
		try {
			System.out.println("Here are the products in " + category);
			Statement stmt = con.createStatement();

			String sql = "select auction_id, name, description from Product where status = 'under auction' AND auction_id in (SELECT AUCTION_ID from BelongsTo where category = '"
					+ category + "')";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println("-----------------------");
				System.out.println("ID: " + rs.getInt("auction_id"));
				System.out.println("Name: " + rs.getString("name"));
				System.out.println("Description: " + rs.getString("description"));

			}
		} catch (Exception e) {
			System.out.println("Could not list products: " + e);
		}

	}

	static void prodsByPrice() {
		ResultSet rs = null;
		try {
			String sql = "select auction_id, name, description,amount from Product where amount is not null and status = 'under auction' order by amount desc";
			rs = MyAuction.query(sql);
			while (rs.next()) {
				System.out.println("-----------------------");
				System.out.println("ID: " + rs.getInt("auction_id"));
				System.out.println("Name: " + rs.getString("name"));
				System.out.println("Description: " + rs.getString("description"));
				System.out.println("Price: " + rs.getInt("amount"));

			}
		} catch (Exception e) {
			System.out.println("Listing prods by price failed: " + e);
		
		try{
			rs.close();
		}
		catch(Exception f){
			System.out.println("Could not close result" + f);
		}}

	}

	static void prodsByAlpha() {
		ResultSet rs = null;
		try {
			String sql = "select auction_id, name, description from Product where status = 'under auction' order by name asc";
			rs = MyAuction.query(sql);
			while (rs.next()) {
				System.out.println("-----------------------");
				System.out.println("ID: " + rs.getInt("auction_id"));
				System.out.println("Name: " + rs.getString("name"));
				System.out.println("Description: " + rs.getString("description"));

			}
		} catch (Exception e) {
			System.out.println("Listing prods by price failed: " + e);
		}
		try{
			rs.close();
		}
		catch(Exception e){
			System.out.println("Could not close result" + e);
		}


	}

	public static ArrayList<String> getSubCategories(String cat) {
		ArrayList<String> categories = null;
		try {
			Statement stmt = con.createStatement();
			String sql = null;
			if (cat == null) {
				sql = "SELECT name from Category where parent_category is null";
			} else {
				sql = "SELECT name from Category where parent_category = '" + cat + "'";
			}

			ResultSet rs = stmt.executeQuery(sql);
			categories = new ArrayList<String>();

			while (rs.next()) {
				categories.add(rs.getString("name"));
			}
		} catch (Exception e) {
			System.out.println("could not retrieve categories: " + e);
		}
		return categories;
	}

	public static void displayCategories(ArrayList<String> categories) {
		int count = 0;
		for (String category : categories) {
			System.out.println("(" + count + ")" + category);

			count++;
		}
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}

}