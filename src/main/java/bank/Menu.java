package bank;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import bank.exception.AmountException;

public class Menu {
  private Scanner scanner;

  public static void main(String[] args) {
    System.out.println("Welcome to the International Bank");
    Menu menu = new Menu();
    menu.scanner = new Scanner(System.in);

    Customer customer = menu.authenticateUser();

    if (customer != null) {
      Account account = DataSource.getAccount(customer.getAccountId());
      menu.showMenu(customer, account);
    }
    menu.scanner.close();
  }

  private Customer authenticateUser() {
    System.out.println("Please enter your username");
    String username = scanner.next();

    System.out.println("Please enter your password");
    String password = scanner.next();

    Customer customer = null;
    try {
      customer = Authenticator.logIn(username, password);
    } catch (LoginException e) {
      System.out.println("An error occured: " + e.getMessage());
    }

    return customer;
  }

  private void showMenu(Customer customer, Account account) {
    int selection = 0;

    while (selection != 4 && customer.isAuthenticated()) {
      System.out.println("==========================================");
      System.out.println("Please select one of the following operations to carry out");
      System.out.println("1. Deposit");
      System.out.println("2. Withdraw");
      System.out.println("3. Check Balance");
      System.out.println("4. Logout and Exit");
      System.out.println("==========================================");

      selection = scanner.nextInt();
      double amount;

      switch (selection) {
        case 1:
          System.out.println("How much do you want to deposit?");
          amount = scanner.nextDouble();
          try {
            account.deposit(amount);
          } catch (AmountException e) {
            System.out.println(e.getMessage());
            System.out.println("Please try again!");
          }
          break;

        case 2:
          System.out.println("How much do you want to withdraw?");
          amount = scanner.nextDouble();
          try {
            account.withdraw(amount);
          } catch (AmountException e) {
            System.out.println(e.getMessage());
            System.out.println("Please try again!");
          }
         
          break;

        case 3:
          System.out.println("The account balance is: " + account.getBalance());
          break;

        case 4:
          Authenticator.logOut(customer);
          System.out.println("Thank you for banking with us");
          break;

        default:
          System.out.println("Invalid option!. Please select value between 1 to 4");
          break;
      }

    }
  }
}
