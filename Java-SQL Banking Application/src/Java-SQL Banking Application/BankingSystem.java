package prog1;

//THIS CLASS IS SQLPROCEDURES

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.PreparedStatement;
/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	
	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	public static void BankingSystem() { // to allow to use object
		
	}
	
	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
			} catch (Exception e) {
				System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
				e.printStackTrace();
			}
	  }

	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin)
	{
		String query = "insert into Customer (name, gender, age, pin) values (?, ?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, gender);
			ps.setInt(3, Integer.parseInt(age));
			ps.setInt(4, Integer.parseInt(pin));
			ResultSet rs = ps.executeQuery();
			rs.next(); // to iterate to the customer ID
			System.out.println(("Customer ID is: " + rs.getInt("id")));
			con.close();
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			CallableStatement cs = con.prepareCall("{call insertCustomer(?,?,?,?)}");
//			cs.setString(1, name);
//			cs.setString(2, gender);
//			cs.setInt(3, age);
//			cs.setInt(4, pin);
//			ResultSet rs = cs.executeQuery();
//			rs.next(); // iterate to the ID
//			System.out.println("Customer ID is: " + rs.getInt("ID")); // prints out the customer ID
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(":: CREATE NEW CUSTOMER - SUCCESS");
	}

	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount) 
	{
		String query = "insert into Account (id, type, amount) values(?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, Integer.parseInt(id));
			ps.setString(2, type);
			ps.setInt(3, Integer.parseInt(amount));
			ResultSet rs = ps.executeQuery();
			rs.next(); // iterate to the first column
			System.out.println("Account ID is: " + rs.getInt("Number"));
			ps.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(":: OPEN ACCOUNT - SUCCESS");
	}

	/**
	 * Close an account.
	 * Update balance to zero and change the status to Inactive
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) 
	{
		String query = "UPDATE Account SET status = ?, balance = ? WHERE number = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, "I");
			ps.setInt(2, 0);
			ps.setInt(3, Integer.parseInt(accNum));
			ResultSet rs = ps.executeQuery(); // dont need this?
			ps.executeUpdate();
			ps.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(":: CLOSE ACCOUNT - SUCCESS");
	}

	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) 
	{
		String query = "update account set balance = ? where number = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			ps.setInt(1, rs.getInt("balance") + Integer.parseInt(amount)); // updates current + deposit
			ps.setInt(2, Integer.parseInt(accNum));
			ps.executeUpdate();
			ps.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(":: DEPOSIT - SUCCESS");
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) 
	{
		String query = "update account set balance = ? where number = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if (rs.getInt("balance") > Integer.parseInt(amount)) { // IF THERE is enough money to withdraw
				ps.setInt(1, rs.getInt("balance") - Integer.parseInt(amount)); // updates current - withdrawal
				ps.setInt(2, Integer.parseInt(accNum));
				ps.executeUpdate();
				ps.close();
				rs.close();
				con.close();
				System.out.println(":: WITHDRAW - SUCCESS");
			}
			else {
				System.out.println("There is not enough money to withdraw.");
				System.out.println(":: WITHDRAW - FAIL");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) 
	{
		// get source# 
		// if amount being transferred is less than total balance in source#
		// transfer money FROM source# into dest# 
		// else error and say that there isnt enuf money in source#
		
		String query = "update account set balance = ? where number = ?";
		try {
			PreparedStatement withdraw = con.prepareStatement(query);
			PreparedStatement deposit = con.prepareStatement(query);
			ResultSet d = deposit.executeQuery();
			ResultSet rs = withdraw.executeQuery();
			if (rs.getInt("balance") > Integer.parseInt(amount)) { // IF THERE is enough money to withdraw
				withdraw.setInt(1, rs.getInt("balance") - Integer.parseInt(amount)); // updates current - withdrawal
				withdraw.setInt(2, Integer.parseInt(srcAccNum));
				withdraw.executeUpdate();
				withdraw.close();
				
				deposit.setInt(1, d.getInt("balance") + Integer.parseInt(amount));
				deposit.setInt(2,  Integer.parseInt(destAccNum));
				deposit.executeUpdate();
				deposit.close();
				con.close();
				
				System.out.println(":: TRANSFER - SUCCESS");
			}
			else {
				System.out.println("There is not enough money to transfer to destAccNum in srcAccNum."); 
				// no make them go back to the main menu for customer
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Display account summary.
	 * @param accNum account number
	 */
	public static void accountSummary(String accNum) 
	{
		// display account information for said accNum
		String query = "select number, balance from account where number = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, Integer.parseInt(accNum));
			
			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				int accId = Integer.parseInt((rs.getString("ID")));
//				int accBal = Integer.parseInt((rs.getString("balance")));
//			}
			// ^ not sure if need to format OR if SQL select statement does it for us
			ps.close();
			rs.close();
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(":: ACCOUNT SUMMARY - SUCCESS");
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA() 
	{
		
		System.out.println(":: REPORT A - SUCCESS");
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) 
	{
		System.out.println(":: REPORT B - SUCCESS");
	}
}
