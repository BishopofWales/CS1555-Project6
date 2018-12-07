import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;


public class Browsing{
	static Connection con = null;
	static Scanner userIn;

	public static void start(Connection rCon, Scanner rUserIn){
		userIn = rUserIn;
		con = rCon;
		browsing();
	}
    public static void browsing() {
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

			String sql = "select auction_id, name, description from Product where auction_id in (SELECT AUCTION_ID from BelongsTo where category = '"
					+ category + "')";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("auction_id"));
				System.out.println("Name: " + rs.getString("name"));
				System.out.println("Description: " + rs.getString("description"));
				System.out.println("-----------------------");
			}
		} catch (Exception e) {
			System.out.println("Could not list products: " + e);
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