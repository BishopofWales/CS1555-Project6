//productSuggestions.java

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import oracle.jdbc.driver.OracleDriver;

public class productSuggestions {
      //private ArrayList<Product> suggestedProducts;
      //private static PreparedStatement listSuggestedStatement = null;

      static Connection con = null;
      static Scanner userIn;

      public static void start(Connection rCon, Scanner rUserIn) {
            con = rCon;
            userIn = rUserIn;
      }

      public static void run() {


            listSuggestions();
        /*
            try {
                  int suggestedProductAuctionID = Integer.parseInt(option);
                  session.setSelectedAuctionID(suggestedProductAuctionID);
            }
            catch (SQLException e) {
                  while (e != null) {
                        System.out.println(e.toString());
                        e = e.getNextException();
                  }
            }
            */
      }

      public static void listSuggestions() {

            try {
                  String sqlSuggestions = "listSuggestedItems.sql";

                  //sqlSuggestions.setString(1, session.getUsername());
                  //sqlSuggestions.setString(2, session.getUsername());

                  ResultSet rs = MyAuction.query(sqlSuggestions);
                  while (rs.next()) {
                        int[] suggestionsArray = new int[10000];
                        int suggestedAuctionID = rs.getInt("suggested_auction");
                        for (int i = 0; i < suggestionsArray.length; i++) {
                              int index = suggestionsArray[i];
                              suggestionsArray[i] = index;
                        }

                        //Product suggestedProduct = new Product(con, suggestedAuctionId);
                        //addSuggestedProduct(suggestedProduct);
                  }
            }
            catch (SQLException e) {
                  while (e != null) {
                        System.out.println(e.toString());
                        e = e.getNextException();
                  }
            }
      }
}
