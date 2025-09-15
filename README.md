# **ConsoleBank – Java Banking Application**

ConsoleBank is a fully functional console-based banking management system built in Java. It simulates a real-world banking environment where users can create accounts, perform transactions, and manage their funds securely. The system is simple, lightweight, and perfect for learning database connectivity, Java programming, and basic banking operations.

This project demonstrates Java OOP principles, JDBC connectivity, and SQL database management, making it a practical tool for beginners and a great addition to any portfolio.

---

## **Features**
- **Create Account:** Users can create **Checking** or **Saving** accounts with a 4-digit PIN.  
- **Login Existing Account:** Access account by providing **Account Number** and **PIN**.  
- **Deposit Money:** Add money to your account.  
- **Withdraw Money:** Withdraw money from your account (cannot exceed balance).  
- **View Balance:** Check your current account balance.  
- **Close Account:** Delete account and move details to **history table**.  
- **Show All Accounts:** Admin view to display total accounts in bank.

---

## **Getting Started**

### **Prerequisites**
- Java JDK installed  
- Oracle Database (or any compatible SQL DB)  
- IDE like **Eclipse** or **IntelliJ**  

---

### **Running the Program**
1. Compile the program:
-     javac -d bin src/bnk/ms/MainBank.java

3. Run the program:
-     java -cp bin;ojdbc8.jar bnk.ms.MainBank

