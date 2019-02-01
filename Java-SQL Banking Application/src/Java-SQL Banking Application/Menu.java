package prog1;

// THIS CLASS IS BANK.JAVA 

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
	static BankingSystem bank;
	static Scanner in;
	static Input input;
	
	// Main Menu only
	public static void createMain() {
		in = new Scanner(System.in);
		bank = new BankingSystem(); 
		System.out.println("Welcome to the Self Services Banking System!");
		MainMenuOptions op = null;
		while (true) {
			op = input.getMainInput();
			switch (op) {
			case N: 
				// prompt new customer menu
				newCustomer();
				break;
			
			case L: 
				// prompt login menu
				LoginMenu();
				break;
			case E:
				System.out.println("Exiting.");
				in.close();
				System.exit(0);
			}
		}
	}
	
	// ties into createMain
	public static void newCustomer() {
		String name; 
		String gender;
		String age, pin;
		boolean taken = true;
		boolean validPass = false;
		System.out.println("Input account credentials to create your account.");
		System.out.println("Name: ");
		name = in.next(); 
		System.out.println("Gender: ");
		gender = in.next(); // gets the string value at 0
		System.out.println("Age: ");
		age = in.next();
		System.out.println("Pin: ");
		pin = in.next();
//		while (taken) {
//			System.out.println("Choose valid username: ");
//			String username = in.next();
//			// BankingSystem method to check if valid username. this returns a bool
//		}
//		while (!validPass) {
//			System.out.println("Choose password: ");
//			String pass = in.next();
//			System.out.println("Please reconfirm password. ");
//			String confirm = in.next();
//			if (pass.equals(confirm)) validPass = true;
//			else System.out.println("Passwords do not match. Please re-enter passwords.");
//		}
		// inserts into database and returns the CustomerNumber if successful.
		bank.newCustomer(name, gender, age, pin);
	}
	
	// [L]ogin ties into createMain
	public static void LoginMenu() {
		int id, pin = -1; // default
		System.out.println("Login Menu: ");
		System.out.println("Customer ID: ");
		id = Integer.parseInt(in.next());
		System.out.println("PIN: "); {
		pin = Integer.parseInt(in.next());
		// write procedure to validate customer ID
		// if ID and pin are 0 then hardcode to go to an adminPage. 
		// else regular user and go to the CustomerMainMenu
		CustomerMainMenu();
		}
	}
	
	// if user inputs 0 and 0
	public static void adminPage() {
		System.out.println("Admin Options: ");
		while (true) {
			System.out.println("[A]ccount Summary for Customer, [1] Report A, [2] Report B, [E]xit");
			char op = in.next().charAt(0);
			switch (op) {
			case 'A':
				// account summary for customer ID
				break;
			case '1': 
				break;
			case '2': 
				break;
			case 'E':
				break;
			}
		}
	}
	
	public static void customerAccSum() {
		System.out.println("Please input the Customer ID for the summary.");
		int id = Integer.parseInt(in.next());
		// check with BankingSystem to get account#, balance for account#, and total balance.
		
	}
	
	/*
	 * SCREEN 3
	 */
	public static void CustomerMainMenu() {
		System.out.println("Customer Main Menu:");
		while (true) {
			char op = in.next().charAt(0);
			switch (op) {
			case 'O': 
				// open account
				break;
			case 'C':
				// close account
				break;
			case 'D':
				// deposit money into account
				break;
			case 'W':
				// withdraw money from account
				break;
			case 'T': 
				// transfer money into another account
				break;
			case 'A':
				// returns an account summary
				break;
			case 'E':
				System.out.println("Exiting to previous menu.");
				createMain(); // goes back to the previous menu
			}
		}
	}
	
}
