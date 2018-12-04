import java.io.*;
import java.util.*;
import java.sql.*;

public class MyAuction{
	static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	static final String DB_PWD = "4031317";
	static final String DB_USR = "mph47";
	static Scanner userIn;
	static Connection con = null;
	
	public static void main(String[] args) throws Exception{
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
			adminMenu();
		}
		else{
			System.exit(0);
		}
	}
	public static void adminMenu() throws Exception{
		System.out.println("Welcome Administrator! Would you like to:");
		while (true) {
					System.out.println("----------------\n(a)Register A New Customer\n(b)Update The System Date\n(c)Generate Product Statistics\n(d)In-Depth Product Statistic\n(e)Quit");
					String responseLine = userIn.nextLine();
					if(responseLine.length() > 1){
						System.out.println("Please specify the letter for the option you would like");
						continue;
					}
					char responseLetter = responseLine.charAt(0);
					switch(responseLetter){
						case 'e': quitting();

						case 'a':	registerCustomer();
									break;
						case 'b':	updateDate();
									break;
						case 'c':	productStats();
									break;
						case 'd':	inDepthStats();
									break;
						default:	System.out.println("Please select options (a-f) or (q) to quit");
									break;
					}
		}
	}
	public static void custMenu() throws Exception{
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
	public static void browsing() throws Exception{
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
	
	public static void auction() throws Exception{
		String name, description, category, user;
		int numDays, minPrice;
		Statement statement = con.createStatement();
		PreparedStatement prepStatement;
		String query;
		
		System.out.println("Enter your user name:");
		user = userIn.nextLine();
		System.out.println("Enter the name of your product:");
		name = userIn.nextLine();
		System.out.println("Enter a description for your product (optional):");
		description = userIn.nextLine();
		System.out.println("Enter the category of your product:");
		category = userIn.nextLine();
		System.out.println("Enter the amount of days the auction will last:");
		numDays = Integer.parseInt(userIn.nextLine());
		System.out.println("Enter the minimum price you will accept:");
		minPrice = Integer.parseInt(userIn.nextLine());
		
		query = "Call proc_putProduct (?,?,?,?,?,?)";
		prepStatement = con.prepareStatement(query);
		prepStatement.setString(1,name);
		prepStatement.setString(2,description);
		prepStatement.setString(3,user);
		prepStatement.setString(4,category);
		prepStatement.setInt(5,numDays);
		prepStatement.setInt(6,minPrice);
		prepStatement.executeUpdate();
	}
	
	public static void bidding() throws Exception{
		int amount, auctionID;
		Statement statement = con.createStatement();
		PreparedStatement prepStatement;
		String query;
		
		System.out.println("Enter the auction ID to bid on:");
		auctionID = Integer.parseInt(userIn.nextLine());
		System.out.println("Enter the amount you wish to bid:");
		amount = Integer.parseInt(userIn.nextLine());
		
		statement = con.createStatement();
		query = "Update product set amount=? where auction_ID=?";
		prepStatement = con.prepareStatement(query);
		prepStatement.setInt(1, amount);
		prepStatement.setInt(2, auctionID);
		prepStatement.executeUpdate();
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
	
	public static void registerCustomer() throws Exception {
		String name, login, password, address, email, admin;
		Statement statement = con.createStatement();
		PreparedStatement prepStatement;
		String query;
		
		System.out.println("Enter your name:");
		name = userIn.nextLine();
		System.out.println("Enter your login:");
		login = userIn.nextLine();
		System.out.println("Enter your password:");
		password = userIn.nextLine();
		System.out.println("Enter your address:");
		address = userIn.nextLine();
		System.out.println("Enter your email:");
		email = userIn.nextLine();
		System.out.println("Are you an administrator? y/n:");
		admin = userIn.nextLine();
		
		if (admin.equalsIgnoreCase("y")) {
			statement = con.createStatement();
			query = "insert into customer values (?,?,?,?,?)";
			prepStatement = con.prepareStatement(query);
			prepStatement.setString(1,login);
			prepStatement.setString(2,password);
			prepStatement.setString(3,name);
			prepStatement.setString(4,address);
			prepStatement.setString(5,email);
			prepStatement.executeUpdate();
		} else if (admin.equalsIgnoreCase("n")) {
			statement = con.createStatement();
			query = "insert into administrator values (?,?,?,?,?)";
			prepStatement = con.prepareStatement(query);
			prepStatement.setString(1,login);
			prepStatement.setString(2,password);
			prepStatement.setString(3,name);
			prepStatement.setString(4,address);
			prepStatement.setString(5,email);
			prepStatement.executeUpdate();
		} else {
			System.out.println("Invalid response, please answer 'y' or 'n'");
		}
	}
	
	private static void updateDate() {
		
	}
	
	private static void productStats() {
		
	}
	
	private static void inDepthStats() {
		
	}
}