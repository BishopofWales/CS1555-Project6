import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class ProductStats {
	static Connection con = null;
	static Scanner userIn;

	public static void start(Connection rCon, Scanner rUserIn){
		userIn = rUserIn;
		con = rCon;
		productStats();
	}
	static void productStats(){
		
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
	static void allProds(){
		System.out.println("Here are statistics on all products.");
	}
	static void prodsByCust(){
		System.out.println("Here are statistics for customer.");
	}
}