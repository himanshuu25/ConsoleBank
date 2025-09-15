package bnk.ms;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import db.util.DBUtil;

// DETAILS TABLE FOR ACCNO,PIN AND TYPE

public class MainBank {
	public static int getInt(Scanner sc) {
		while(true) {
			try {
			return sc.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Invalid input. Please enter a valid number:");
				sc.nextLine();
			}
		
		}
	}
	public static String  getStr(Scanner sc) {
		String str;
		while(true) {
			str=sc.nextLine();
			if("CHECKING".equalsIgnoreCase(str)||"SAVING".equalsIgnoreCase(str)) {
				return str;
			}
			else {
				System.out.println("Invalid input. Please enter a valid type checking/saving:");
			}
		}
	}
	
	public static void createAcc(Connection con,Scanner sc) throws Exception {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT MAX(ACCNO) FROM DETAILS");// 0 if return null
		String sql = "INSERT INTO DETAILS VALUES(?,?,?)";
		PreparedStatement ps = con.prepareStatement(sql);
		
		System.out.println("Set your pin");
		int pin = getInt(sc);
		while(true) {
			if(pin>999&&pin<=9999) {
				break;
			}
			else {
				System.out.println("Please enter four digit pin");
				 pin=getInt(sc);
			}
		}
		sc.nextLine();
		rs.next();
		int ACCno=rs.getInt(1)+1;
		if(rs.wasNull())
			ACCno=10;
		System.out.println("Please enter the account type checking/saving");
		String type = getStr(sc);
		ps.setInt(1, ACCno);
		ps.setInt(2, pin);
		ps.setString(3, type.toUpperCase());
		ps.executeUpdate();
		System.out.println("Please note your Account details:");
		System.out.println("Account Number: " + ACCno);
		System.out.println("PIN Number: " + pin);
		System.out.println("Account Type: " + type);

		String sql1 = "INSERT INTO ACCOUNTS VALUES(?,?)";
		PreparedStatement ps1 = con.prepareStatement(sql1);
		ps1.setInt(1, ACCno);
		ps1.setInt(2, 0);
		ps1.executeUpdate();
		DBUtil.close(null, st, rs);
		DBUtil.close(null, ps, null);
		DBUtil.close(null, ps1, null);
		
	   
	}

	public static void loginExAcc(Connection con,Scanner sc) throws Exception {
		
		Statement st = con.createStatement();
		PreparedStatement psUpdate=null;
		PreparedStatement psSelect=null;
		ResultSet RS =null;
		ResultSet rs = st.executeQuery("SELECT ACCNO,PIN,ACCTYPE FROM DETAILS");
		try {
			Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		while (rs.next()) {
			m.put(rs.getInt(1), rs.getInt(2));
		}
		
		System.out.println("Enter your Account Number");
		int accno = getInt(sc);
		System.out.println("Enter your PIN ");
		int pin = getInt(sc);
		if (m.containsKey(accno) && m.get(accno)==pin) {
			System.out.println("Login Successful!");
			 psUpdate = con.prepareStatement("UPDATE ACCOUNTS SET BALANCE=? WHERE ACCNO=?");
		     psSelect = con.prepareStatement("SELECT BALANCE FROM ACCOUNTS WHERE ACCNO=?");
			while (true) {
				System.out.println("1.View Balance");
				System.out.println("2.Deposit");
				System.out.println("3.Withdraw");
				System.out.println("4.Exit");
				System.out.println("Enter your choice: ");
				int choice = getInt(sc);
				switch (choice) {
				case 1: {
					psSelect.setInt(1, accno);
					RS = psSelect.executeQuery();
					RS.next();
					int blnc = RS.getInt(1);
					System.out.println("Your current balance is: " + blnc);
					
					break;

				}
				case 2: {
					psSelect.setInt(1, accno);
					 RS = psSelect.executeQuery();
					RS.next();
					int blnc = RS.getInt(1);
					System.out.println("Enter the deposit amount: ");
					int dep = getInt(sc);
					 if(dep<=0) {
						System.out.println("Amount can't be negative or zero");
						break;
					}
					
					psUpdate.setInt(1, blnc + dep);
					psUpdate.setInt(2, accno);
					psUpdate.executeUpdate();
					System.out.println(dep + "Deposited Successfully.");
					break;
				}
				case 3: {
					psSelect.setInt(1, accno);
				    RS = psSelect.executeQuery();
					RS.next();
					int blnc = RS.getInt(1);
					System.out.println("Enter the withdrawl amount: ");
					int wd = getInt(sc);
					if(wd>blnc)
						System.out.println("Insufficient funds.");
					else if(wd<=0) {
						System.out.println("Amount cannot be negative or zero.");
						break;
					}
					else {
					psUpdate.setInt(1, blnc - wd);
					psUpdate.setInt(2, accno);
					psUpdate.executeUpdate();
					System.out.println(wd + "Withdrawn Successfully ");
					}
					break;
				}
				case 4: {
					System.out.println("Exit Successfully");
					return;
				}
				default:{
					System.out.println("Invalid option. Please try again.");
				}
				}

			}

		} else {
			System.out.println("Invalid Account Number or PIN. Please try again.");

		}}
		finally {
		DBUtil.close(null, st, rs);
		DBUtil.close(null,psUpdate,RS);
		DBUtil.close(null,psSelect, null);
		}
		
	}

	public static void showAll(Connection con) throws Exception {
		Statement st = con.createStatement();
		ResultSet rsCount=null;
		ResultSet rs = st.executeQuery("SELECT * FROM DETAILS");

		try{
	
		System.out.println("----------------------------------------------------------------------------------------------------------");
		int checkingCount = 0;
		int savingCount = 0;
		int totalAcc=0;
		rsCount = st.executeQuery("SELECT * FROM DETAILS");
		while (rsCount.next()){
			if ("checking".equalsIgnoreCase(rsCount.getString(3)))
				checkingCount++;
			else if ("saving".equalsIgnoreCase(rsCount.getString(3)))
				savingCount++;
			totalAcc++;
			
		}
		System.out.println("Total Checking Account :- " + checkingCount);
		System.out.println("Total Saving Account :- " + savingCount);
		System.out.println("TOTAL ACCOUNT IN THE BANK :- " + totalAcc);
		
		System.out.println("-----------------------------------------------------------------------------------------------------------");
		
	}finally {
		DBUtil.close(null, st, rs);
		DBUtil.close(null, null, rsCount);
	}
	}

	public static void closeAcc(Connection con,Scanner sc) throws Exception {
		PreparedStatement ps = con.prepareStatement("DELETE FROM DETAILS WHERE ACCNO=? ");
		PreparedStatement PS = con.prepareStatement("DELETE FROM ACCOUNTS WHERE ACCNO=? ");
		PreparedStatement ps1=null;
		ResultSet rs1=null;
		PreparedStatement ps2=null;
		
		try{
			System.out.println("Enter the Account number");
	
		int accno = getInt(sc);
		ps.setInt(1, accno);
		PS.setInt(1, accno);
		
		ps1=con.prepareStatement("SELECT ACCNO,PIN,ACCTYPE FROM DETAILS WHERE ACCNO=?");
        ps1.setInt(1, accno);
        rs1=ps1.executeQuery();
        
        ps2=con.prepareStatement("INSERT INTO history VALUES(?,?,?)");
        if(rs1.next()) {
        ps2.setInt(1, rs1.getInt(1));
        ps2.setInt(2, rs1.getInt(2));
        ps2.setString(3, rs1.getString(3));
        ps2.executeUpdate();
        }
        
		if(ps.executeUpdate()>=1) {
			PS.executeUpdate();
		   System.out.println("your account (" + accno + ") is succesfully deleted ");
		              
		}
		else
			System.out.println("your account is not available or wrong account number");
	}
	finally {
		DBUtil.close(null, ps, rs1);
		DBUtil.close(null, PS,null);
		DBUtil.close(null, ps1,null);
		DBUtil.close(null, ps2, null);
	}
		
	}

	public static void main(String[] args) throws Exception {
		try {
		InputStream in = MainBank.class.getClassLoader().getResourceAsStream("o.properties");
		Properties p = new Properties();
		p.load(in);
		Class.forName(p.getProperty("driver"));
		Connection con = DriverManager.getConnection(p.getProperty("url"), p);
		Scanner sc = new Scanner(System.in);
		try{
			while (true) {
			System.out.println("1.Create Account");
			System.out.println("2.Login Existing Account");
			System.out.println("3.Show All Account");
			System.out.println("4.Close Account");
			System.out.println("5.Exit");
			System.out.println("enter your choice");
			int choice =getInt(sc);
			switch (choice) {
			case 1: {
				createAcc(con,sc);
				break;
			}
			case 2: {
				loginExAcc(con,sc);
				break;
			}
			case 3: {
				showAll(con);
				break;
			}
			case 4: {
				closeAcc(con,sc);
				break;
			}
			case 5: {
				System.out.println("successfully exit ");
				sc.close();
			    System.exit(0);
			}
			default: {
				System.out.println("please enter valid option ");
			}
			}
		}
		}
	finally {
		DBUtil.close(con, null, null);
	
	}
	}catch(Exception e) {
		System.out.println("Unexpected Exeption please try again :-"+e.getMessage());
	}
	}
}
