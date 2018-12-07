import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Searching {
	static Connection con = null;
	static Scanner userIn;

	public static void start(Connection rCon, Scanner rUserIn){
		userIn = rUserIn;
		con = rCon;
		searching();
	}
	public static void searching(){
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
			return;
		}
		if (keywords.length <= 0) {
			System.out.println("At least one keyword.");
			return;
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
}