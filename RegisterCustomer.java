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
	
	public static ResultSet query(String query, List<String> parameters) {
		try {
			PreparedStatement pStatement = con.prepareStatement(query);
			for (int i = 1; i <= parameters.size(); i++) {
				pStatement.setString(i, parameters.get(i - 1));
			}
			return pStatement.executeQuery();
		} catch (SQLException e) {
			System.out.println("ERROR RUNNING QUERY: " + e.toString());
			return null;
		}
	}
	
	public static String getUserInput(String prompt) {
		System.out.println(prompt + ": ");
		return userIn.nextLine().trim();
	}
	
	public static void registerCustomer() {
		String name, login, password, address, email, admin;
		ArrayList<String> params = new ArrayList<String>();
		
		//Get data from the user and add it to the parameter list
		name = getUserInput("Enter a name");
		login = getUserInput("Enter a login");
		password = getUserInput("Enter a password");
		address = getUserInput("Enter an address");
		email = getUserInput("Enter an email");
		admin = getUserInput("Is this user an administrator? y/n");
		
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
			return query("Insert into administrator values (?,?,?,?,?)", params);
		} else if (admin.equalsIgnoreCase("n")) {	
			//Call the query(String querym List<String> params) method with an insert on customer
			return query("Insert into customer values (?,?,?,?,?)", params);	
		} else {
			System.out.println("Invalid response, please answer 'y' or 'n'");
			return null;
		}
	}
}