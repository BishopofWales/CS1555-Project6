//Update System Time

import java.awt.*;
import java.util.*;
import java.text.*;
import java.sql.*;
//import myAuction.DBSession;

public class updateSystemTime {
    static final String DB_URL = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
    static final String DB_PWD = "4031317";
    static final String DB_USR = "mph47";
    static Connection con;
    static String currentTime;
    static SimpleDateFormat dateFormat;


      public static void run() {
            Scanner input = new Scanner(System.in);

            System.out.println("PLEASE FOLLOW THE PROMPTS TO ENTER THE DESIRED DATE");
            System.out.println("BE SURE TO RESPOND TO EACH PROMPT WITH AN INT (NOT A STRING)");
            System.out.println("____________________________________________________________");

            System.out.println("PLEASE ENTER THE MONTH: ");
            String month = input.nextLine();
            System.out.println("PLEASE ENTER THE DAY: ");
            String day = input.nextLine();
            System.out.println("PLEASE ENTER THE YEAR: ");
            String year = input.nextLine();
            System.out.println("PLEASE ENTER THE HOUR OF THE DAY: ");
            String hour = input.nextLine();
            System.out.println("PLEASE ENTER THE MINUTE OF THE HOUR: ");
            String minute = input.nextLine();
            System.out.println("PLEASE ENTER THE SECOND OF THE MINUTE: ");
            String second = input.nextLine();

            formatDate(month, day, year, hour, minute, second);

      }

//format the date into desired format for SQL entry
      public static String formatDate(String month, String day, String year, String hour, String minute, String second) {
        if (month.length() == 1) {
              month = "0" + month;
        }

        if (day.length() == 1) {
              day = "0" + day;
        }

        if (hour.length() == 1) {
              hour = "0" + hour;
        }

        if (minute.length() == 1) {
              minute = "0" + minute;
        }

        if (second.length() == 1) {
              second = "0" + second;
        }
        //System.out.println(month + day + year + hour + minute + second);

            ArrayList<String> params = new ArrayList<String>();
            params.add(month);
            params.add(day);
            params.add(year);
            params.add(hour);
            params.add(minute);
            params.add(second);
            //System.out.println(params);

            String theDateString = month + "/" + day + "/" + year + " " + hour +":" + minute + ":" + second;

            ArrayList<String> paramsString = new ArrayList<String>();
            paramsString.add(theDateString);
            //System.out.println(theDateString);

            //System.out.println(paramsString);


            String sqlDate = "update ourSysDATE set c_date = to_date(?, 'MM-DD-YYYY HH:MI:SS')";
            ResultSet rs = null;
              try {
                rs = MyAuction.query(sqlDate, paramsString);
                System.out.println(rs.toString());
                //if (rs != null) {
                //while (rs.next()) {
                      //System.out.println("UPDATED DATE: " + rs.getDate("c_date"));
                      //java.sql.Date dbSqlDate = rs.getDate("c_date");
                      //java.sql.Time dbSqlTime = rs.getTime("c_date");



            }
            catch(Exception e) {
              while (e != null) {
                    System.out.println(e.toString());
                    //e = e.getNextException();
              }
            }

            return month + "/" + day + "/" + year + " " + hour + ":" + minute + ":" + second;

      }

/*
//update the time in SQL
      public int updateTime(String month, String day, String year, String hour, String minute, String second) {
            try {
                  updateTimeChoice.setString(1, formatDate(month, day, year, hour, minute, second));
                  return updateTimeChoice.executeUpdate();
            }
            catch (SQLException e) {
                  while (e != null) {
                        debug.println(e.toString());
                        debug.flush();
                        e = e.getNextException();
                  }
            }
            return -1;
      }
*/
}
