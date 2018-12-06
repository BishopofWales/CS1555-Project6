import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class MyAuction{
	static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	static final String DB_PWD = "4031317";
	static final String DB_USR = "mph47";
	static Scanner userIn;
	static String query, username, password;
	static SimpleDateFormat dateFormat;

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
			adminMenu();
		}
		else{
			System.exit(0);
		}
	}

	//Verify login credentials
	public boolean login(int type) {
	/*
	type 2 = user (login)
	*/
	            try {
	                  System.out.println("PLEASE ENTER YOUR LOGIN CREDENTIALS");
	                  username = getUserInput("Username");
	                  password = getUserInput("Password");

	                  ResultSet resultSet;
	                  if (type == 2) {
	                        resultSet = query("SELECT LOGIN, PASSWORD FROM USER");
	                  }
	                  else {
	                        resultSet = query("SELECT LOGIN, PASSWORD FROM ADMINISTRATOR");
	                  }

	                  while (resultSet.next()) {
	                        if (username.equals(resultSet.getString(1)) && password.equals(resultSet.getString(2))) {
	                              return true; //username and password combo is correct
	                        }
	                  }
	                  return false;
	            }

	            catch (SQLException e) {
	                  System.out.println("ERROR RUNNING QUERIES: " + e.toString());
	            }
	            return false;
	      }

////////////////////////////////////////////////////////////////////////////////
//ADMINISTRATOR MENU ///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

	public static void adminMenu(){
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

	//Helpers
	      public static String getUserInput(String prompt) {
	            System.out.println(prompt + ": ");
	            return userIn.nextLine().trim();
	      }

	      public static ResultSet query(String query) {
	            try {
	                  Statement s = con.createStatement();
	                  return s.executeQuery(query);
	            }
	            catch(SQLException e) {
	                  System.out.println("ERROR RUNNING DATABASE QUERY: " + e.toString());
	                  return null;
	            }
	      }

	      public static ResultSet query(String query, List<String> parameters) {
	            try {
	                  PreparedStatement pStatement = con.prepareStatement(query);
	                  for (int i = 1; i <= parameters.size(); i++) {
	                        pStatement.setString(i, parameters.get(i - 1));
	                  }
	                  return pStatement.executeQuery();
	            }
	            catch (SQLException e) {
	                  System.out.println("ERROR RUNNING QUERY: " + e.toString());
	                  return null;
	            }
	      }
//Update System Date
	public static void updateDate() {

					String date;
					date = getUserInput("\n PLEASE SET THE DATE (PLEASE FOLLOW FORMAT DD-MM-YYYY/HH:MI:SS)");
					ResultSet resultSet;
					resultSet = query("update sys_time set my_time = to_date('" + date + "', 'dd-mm-yyyy/hh:mi:ssam'");
					if (resultSet == null) {
								System.out.println("PLEASE ENTER DATE IN CORRECT FORMAT");
					}
					else {
								System.out.println("UPDATE SUCCESSFUL");
					}

	}

//Register Customer
	public static void registerCustomer() {
				System.out.println("Registering Customer");
	}

//Product Stats
	public static void productStats() {
				System.out.println("Product Stats");
	}

//In-Depth Product Stats
	public static void inDepthStats() {
				System.out.println("In Depth Stats");
	}
////////////////////////////////////////////////////////////////////////////////
//CUSTOMER MENU ////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

	public static void custMenu(){
		System.out.println("Welcome Customer! Would you like to:");
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
