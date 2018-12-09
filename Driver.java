import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class Driver {
    public static void main() {
        System.out.println("Listing all root categories:");
        ArrayList<String> rootCats = Browsing.getSubCategories(null);
        for (String cat : rootCats) {
            System.out.println(cat);
        }

    }
}