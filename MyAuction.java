import java.io.*;
import java.util.*;
import java.sql.*;

public class MyAuction {
	// javac -classpath '.;.\ojdbc6.jar' MyAuction.java
	static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	static final String DB_PWD = "4031317";
	static final String DB_USR = "mph47";
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

	public static void adminMenu() {
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
			case 'e':
				quitting();

			case 'a':
				registerCustomer();
				break;
			case 'b':
				updateDate();
				break;
			case 'c':
				productStats();
				break;
			case 'd':
				inDepthStats();
				break;
			default:
				System.out.println("Please select options (a-f) or (q) to quit");
				break;
			}
		}
	}

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
				browsing();
				break;
			case 'b':
				searching();
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

	public static void displayCategories(ArrayList<String> categories) {
		int count = 0;
		for (String category : categories) {
			System.out.println("(" + count + ")" + category);

			count++;
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

	public static void searching() {
		// to do: refine regular expression so that it matches only with words, not
		// subsets of words
		// \s is the whitespace character.
		System.out.println("Please enter up to two keywords, seperated by a space. Results will match BOTH keywords");
		String responseLine = userIn.nextLine();
		String[] keywords = responseLine.split(" ");
		for (int i = 0; i < keywords.length; i++) {
			System.out.println(keywords[i]);
		}
		if (keywords.length > 2) {
			System.out.println("No more than two keywords.");
			custMenu();
		}
		if (keywords.length <= 0) {
			System.out.println("At least one keyword.");
			custMenu();
		}
		try {
			Statement stmt = con.createStatement();
			String sql = null;
			if (keywords.length == 1) {
				sql = "select * from product where REGEXP_LIKE(description,'.*" + keywords[0] + ".*')";
			} else {
				sql = "select * from product where REGEXP_LIKE(description,'.*" + keywords[0]
						+ ".*') and REGEXP_LIKE(description,'.*" + keywords[1] + ".*')";
			}
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("name"));
				System.out.println(rs.getString("description"));
				System.out.println("-----------------");
			}
		} catch (Exception e) {
			System.out.println("Search failed:" + e);
		}
	}

	public static void auction() {
		System.out.println("Auction");
	}

	public static void bidding() {
		System.out.println("Bidding");
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

	<<<<<<<HEAD

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}
}=======}>>>>>>>origin/jaj110
