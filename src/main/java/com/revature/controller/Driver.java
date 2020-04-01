package com.revature.controller;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import com.revature.exception.DuplicateUsernameException;
import com.revature.exception.PasswordTooShortException;
import com.revature.model.Account;
import com.revature.repository.AccountDao;

public class Driver {
  
  /**
   * Scanner for reading console input.
   */
  private static Scanner sc = new Scanner(System.in);
  private static AccountDao adao = new AccountDao();
  private static Account loggedInAccount = null;
  /**
   * All user accounts created.  We don't have file or DB storage just yet.
   */
  private static Set<Account> accounts = new HashSet<Account>();
  
  /**
   * Whether the user is currently logged in
   */
  private static boolean loggedIn = false;

  public static void start() {
    
    //while the user is not logged in, loop welcome menu:
    while(!Driver.loggedIn) {
      int welcomeMenuOutput = runWelcomeMenu();
      if(welcomeMenuOutput == 0) {
        System.exit(0);
      }
    }
    //Run the menu repeatedly while logged in, until the runMenu method returns 0, telling us to exit.
    while(Driver.loggedIn) {
      int menuOutput = runMenu();
      if(menuOutput == 0) {
        break;
      }
    }
  }
  
  /**
   * Runs the login screen
   */
  public static void runLogin() {
    System.out.println("Please log in.  Provide username:");
    String usernameInput = sc.nextLine();
    System.out.println("Provide password:");
    String passwordInput = sc.nextLine();

    Account a = adao.findByString(usernameInput);
    try {
     
  a.getUsername();
    } catch (Exception e) {
      System.out.println("That account does not exist - please create an account?\n");
      return;
    }
  
   if(a.getPassword().equals(passwordInput)){
      Driver.loggedIn = true;
      loggedInAccount = a;
    }
  
    if(!Driver.loggedIn) {
      System.out.println("Failed to log in with your username and password.");
    }
  }
  
  /**
   * Runs the welcome menu that provides only 3 options: login, create account, and exit.
   * 
   * @return 0 if the program should exit, 1 otherwise
   */
  public static int runWelcomeMenu() {
  
    System.out.println("Welcome to the K.I.S.S. Bank Account Menu: ");
    System.out.println("---------------------------------------\n");
    System.out.println("Enter 1 to Create an account ");
    System.out.println("Enter 2 to Login ");
    System.out.println("Enter 0 to Exit \n");
    System.out.println("---------------------------------------\n");
    
    
    String userOption = sc.nextLine();
    switch(userOption) {
      case "1":
        createAccount();
        return 1;
      case "2":
        runLogin();
        return 1;
      case "0":
        System.out.println("Thank you, and have a nice day!\n");
        return 0;
      default:
        System.out.println("Failed to recognize option");
        return 1;
    }
    
  }
  
  /**
   * Runs a menu that lets the user select a feature of our program.
   * Returns a 0 if the program should exit, returns a 1 otherwise.
   */
  public static int runMenu() {
    System.out.println("Welcome to the your K.I.S.S. Bank Menu:");
    System.out.println("--------------------------------------\n");
    System.out.println("Choose 1 to check balance.");
    System.out.println("Choose 2 to make a deposit.");
    System.out.println("Choose 3 to make a withdrawal");
    System.out.println("Choose 4 to create Account");
    //System.out.println("Choose Accounts to list all Accounts \n");
    System.out.println("--------------------------------------\n");
    
    
    System.out.println("Choose 0 to exit.");
    String userOption = sc.nextLine();
    switch(userOption) {
      case "1":
        runBalance();
        return 1;
      case "2":
        runDeposit();
        return 1;
      case "3":
        runWithdrawal();
        return 1;
      case "4":
        createAccount();
        return 1;
      
      case "0":
        System.out.println("Thank you, and have a nice day!\n");
        return 0;
      case "debuglogin":
        runLogin();
        return 1;
      default:
        System.out.println("failed to recognize option");
        return 1;
    }
  }
  
  private static void runDeposit() {
    @SuppressWarnings("resource")
    Scanner myScan = new Scanner(System.in);
    Account a = adao.findByString(loggedInAccount.getUsername());
    System.out.println("How much are you depositing?\n");
    
    Double depositAmount = myScan.nextDouble();
    adao.update(a, depositAmount+a.getBalance());
    
    a = adao.findByString(loggedInAccount.getUsername());
    loggedInAccount = a;
    
    System.out.println(loggedInAccount.getBalance());
    
  }

  
  private static void runWithdrawal() {
    @SuppressWarnings("resource")
    Scanner myScan = new Scanner(System.in);
    Account a = adao.findByString(loggedInAccount.getUsername());
    System.out.println("How much are you withdrawing?\n");
    System.out.println("--------------------------------------\n");
    Float withdrawAmount = myScan.nextFloat();
    //adao.update(a, withdrawAmount+a.getBalance());
    adao.update(a, a.getBalance()-withdrawAmount);
    
    a = adao.findByString(loggedInAccount.getUsername());
    loggedInAccount = a;
    
    System.out.println(loggedInAccount.getBalance());
    
  }
  
  private static void runBalance() {
    System.out.println("Your new balance is: $"+ loggedInAccount.getBalance()+"0");
    System.out.println("--------------------------------------\n");
    
  }

  public static void createAccount() {
    System.out.println("Welcome to Account creation.");
    //just one retry count, we'll let the user retry until 3 retries then boot them
    int retryCount = 0;
    while(retryCount<3) {
      System.out.println("Please provide a username:");
      String username =  sc.nextLine();
      System.out.println("Please provide a password:");
      String password = sc.nextLine();
      System.out.println("Please provide a name for the Account");
      String name = sc.nextLine();
      try {
        //attempt to create new account:
        Account account = new Account(username, password, name, 0);
        //add newly created account to our registry of Accounts

        adao.insert(account);
        Driver.loggedIn = true;
        loggedInAccount = adao.findByString(username);
        break;
      } catch (PasswordTooShortException e) {
        System.out.println("Password too short, please retry with password of 8 more characters");
        retryCount++;
      } catch (DuplicateUsernameException e) {
       
        System.out.println("Username already exists in our system, please retry with another");
        retryCount++;
      }
    }
    //for better UX:
    if(retryCount >=3) {
      System.out.println("Retries exceeded, exiting account creation.");
    }
    
  }
  
}