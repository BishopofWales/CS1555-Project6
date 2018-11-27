import java.io.*;
import java.util.*;

public class MyAuction{
	static Scanner userIn;
	public static void main(String[] args){
		userIn = new Scanner(System.in);
		custMenu();
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
				case 'q': System.exit(0);
				
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
		System.out.println("Browsing");
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
}