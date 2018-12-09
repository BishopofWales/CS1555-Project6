//LoginCredentials.java

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import oracle.jdbc.driver.OracleDriver;
//import CS1555-Term-Projcet-master.DBSession;

public class LoginCredentials {
      static Connection con = null;
      static Scanner userIn;

      public static void start(Connection rCon, Scanner rUserIn) {
            userIn = rUserIn;
            con = rCon;
      }
/*
      public void dBsession() {
            try {
                  DriverManager.registerDriver(new OracleDriver());
                  con = DriverManager.getConnection("jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass", "mph47", "4031317");
            }
            catch (Exception e) {
                  System.out.println(e);
            }
            run();
      }
*/
/*
      private static PreparedStatement isCustomerDBStatement = null;
      private static PreparedStatement isAdminDBStatement = null;
*/
/*
      public static String getUserInput(String prompt) {
            System.out.println(prompt + ":");
            return userIn.nextLine.trim()
      }
*/
      public static void run() {
        Scanner input = new Scanner(System.in);

        System.out.println("PLEASE ENTER YOUR USERNAME AND PASSWORD");
        System.out.println("_______________________________________");

        System.out.println("USERNAME: ");
        String userName = input.nextLine();
        System.out.println("PASSWORD: ");
        String password = input.nextLine();

//check given username and password to validate them -1 = not existant, 0 = customer, 1 = admin
        int result = validateUser(userName, password);
        if (result == -1) {
              System.out.println("USERNAME OR PASSWORD IS INCORRECT, PLEASE TRY AGAIN");
              run();
        }
        if (result == 1) {
              MyAuction.adminMenu();
        }
        MyAuction.custMenu();
      }
/*
      public static ResultSet query(String query) {
            try {
              Statement s = con.createStatement();
              return s.executeQuery(query);
            }
            catch (SQLException e) {
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
            } catch (SQLException e) {
                System.out.println("ERROR RUNNING QUERY: " + e.toString());
                return null;
            }
      }

*/
//validates entered username and password
      public static int validateUser(String userName, String password) {
            ArrayList<String> params = new ArrayList<String>();
            params.add(userName);
            params.add(password);

            String sqlCust = "select count(*) as num_customers from Customer where login = ? and password = ?";
            String sqlAdmin = "select count(*) as num_admins from Administrator where login = ? and password = ?";
            ResultSet rsCust = MyAuction.query(sqlCust, params);
            ResultSet rsAdmin = MyAuction.query(sqlAdmin, params);
            try {
                  if (rsCust.next()) {
                      if (rsCust.getInt("num_customers") > 0) {
                            return 0;
                      }
                  }
                  if (rsAdmin.next()) {
                    if (rsAdmin.getInt("num_admins") > 0) {
                          return 1;
                    }
                  }

            }
            catch (SQLException e) {
                  while (e != null) {
                        System.out.println(e.toString());
                        e = e.getNextException();
                  }
            }

            return -1;
      }
}
