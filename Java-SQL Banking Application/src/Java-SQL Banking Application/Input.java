package prog1;

import java.util.Scanner;
// THIS CLASS IS USERINPUT

// this class checks input for menu and validates. If valid 
public class Input {
	
	Scanner in; 
	BankingSystem bank;
	
	/**
	 * Initializes Input object
	 * @param input
	 * @param bank
	 */
	public Input (Scanner input, BankingSystem bank) {
		this.in = input;
		this.bank = bank;
	}
	
	public CustomerOptions CustomerInput() {
		try {
			System.out.println("Choose from Customer Account options.");
			System.out.println("[O]pen Account");
			System.out.println("[C]lose Account");
			System.out.println("[D]eposit");
			System.out.println("[W]ithdraw");
			System.out.println("[T]ransfer");
			System.out.println("[A]ccount Summary");
			System.out.println("[E]xit");
			return CustomerOptions.valueOf(in.next());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Invalid input.");
			return CustomerInput();
		}
	}
	
	public MainMenuOptions getMainInput() {
		try {
			System.out.println("Choose from MainMenu options: ");
			System.out.println("[N]ew Customer");
			System.out.println("[L]ogin");
			System.out.println("[E]xit");
			return MainMenuOptions.valueOf(in.next()); // reads in the next user input when prompted
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Invalid input");
			return getMainInput(); // does a call again to prompt 
		}
	}
	

}
