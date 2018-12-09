import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class RegisterCustomer {

	static Connection con = null;
	static Scanner userIn;
	
	public static void start(Connection rCon, Scanner rUserIn) {
		con = rCon;
		userIn = rUserIn;
	}
	
	public static void registerCustomer() {
		String name, login, password, address, email, admin;
		ArrayList<String> params = new ArrayList<String>();
		
		//Get data from the user and add it to the parameter list
		name = MyAuction.getUserInput("Enter a name");
		login = MyAuction.getUserInput("Enter a login");
		password = MyAuction.getUserInput("Enter a password");
		address = MyAuction.getUserInput("Enter an address");
		email = MyAuction.getUserInput("Enter an email");
		admin = MyAuction.getUserInput("Is this user an administrator? y/n");
		
		params.add(login);
		params.add(password);
		params.add(name);
		params.add(address);
		params.add(email);
		
		
		ResultSet resultSet = null;
		
		try {
			//Get the results from the insert on customer or administrator
			resultSet = registerCustomerQuery(params, admin);
		} catch (Exception e) {
			System.out.println("Error registering customer: " + e.toString());
		}
				
		if (resultSet == null) {
			System.out.println("Error registering customer");
		} else {
			System.out.println("\nCustomer registered successfully\n");
		}
	}
	
	public static ResultSet registerCustomerQuery(ArrayList<String> params, String admin) {
		//Check if the customer will be an administrator or not
		if (admin.equalsIgnoreCase("y")) {
			//Call the query(String query, List<String> params) method with an insert on administrator
			return MyAuction.query("Insert into administrator values (?,?,?,?,?)", params);
		} else if (admin.equalsIgnoreCase("n")) {	
			//Call the query(String querym List<String> params) method with an insert on customer
			return MyAuction.query("Insert into customer values (?,?,?,?,?)", params);	
		} else {
			System.out.println("Invalid response, please answer 'y' or 'n'");
			return null;
		}
	}
}