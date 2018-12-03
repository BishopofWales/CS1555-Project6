import java.io.*;
import java.util.*;
import java.sql.*;

public class MyAuction{
	static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	static final String DB_PWD = "4031317";
	static final String DB_USR = "mph47";
	static Scanner userIn;

	static Connection con = null;
	public static void main(String[] args){
		System.out.println(System.getProperty("java.class.path"));
		userIn = new Scanner(System.in);
			
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");	
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection( DB_URL,DB_USR, DB_PWD );
		}
		catch(Exception e){
			System.out.println("Did not connect to database." + e);
		}
		finally{

		}
		System.out.println("(u)ser or (a)dmin?");
		String responseLine = userIn.nextLine();
		char responseLetter = responseLine.charAt(0);
		if(responseLetter == 'u'){
			custMenu();
		}
		else if(responseLetter == 'a'){
			quitting();
		}
		else{
			System.exit(0);
		}
	}
	public static void adminMenu(){

	}
	public static void custMenu(){
		System.out.println("Welcome! Would you like to:");
		while(true){
			System.out.println("----------------\n(a)Browse\n(b)Search\n(c)Sell\n(d)Bid\n(e)Sell\n(f)Get suggestions\n(q)Quit");
			String responseLine = userIn.nextLine();
			if(responseLine.length() > 1){
				System.out.println("Please specify the letter for the option you would like");
				continue;
			}
			char responseLetter = responseLine.charAt(0);
			switch(responseLetter){
				case 'q': quitting();

				case 'a':	browsing();
							break;
				case 'b':	searching();
							break;
				case 'c':	auction();
							break;
				case 'd':	bidding();
							break;
				case 'e':	selling();
							break;
				case 'f':	suggestions();
							break;
				default:	System.out.println("Please select options (a-f) or (q) to quit");
							break;
			}
			
		}
		
	}
	public static void browsing(){
		try{
			System.out.println("Browsing");
			Statement stmt = con.createStatement();
			String sql = "SELECT auction_id, name, description from Product ";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int auction_id = rs.getInt("auction_id");
				String name = rs.getString("name");
				String description = rs.getString("description");
				System.out.println(name);
				System.out.println(auction_id);
				System.out.println(description);
			}
		}
		catch(Exception e){

		}
		//attributes,  e.g,  auction  id,  name,  description  (if  through  search  as  mentioned  in  task
		//(b)), highest bid amount


	}
	public static void searching(){
		System.out.println("Searching");
	}
	public static void auction(){
		System.out.println("Auction");
	}
	public static void bidding(){
		System.out.println("Bidding");
	}
	public static void selling(){
		System.out.println("Selling");
	}
	public static void suggestions(){
		System.out.println("Suggestions");
	}
	public static void quitting(){
		try{
			con.close();
		}
		catch(Exception e){
			System.out.println("Could not close connection.");
		}
		System.exit(0);
	}
}