package banking;

/**
 * Instruction:
 * You should allow customers to create a new account in the banking system.
 *
 * Once the program starts, you should print the menu:
 *
 * 1. Create an account
 * 2. Log into account
 * 0. Exit
 *
 * If the customer chooses ‘Create an account’, you should generate a new card number which satisfies all the conditions described above. Then you should generate a PIN code that belongs to the generated card number. A PIN code is a sequence of any 4 digits. PIN should be generated in a range from 0000 to 9999.
 *
 * If the customer chooses ‘Log into account’, you should ask them to enter their card information. Your program should store all generated data until it is terminated so that a user is able to log into any of the created accounts by a card number and its pin. You can use an array to store the information.
 *
 * After all information is entered correctly, you should allow the user to check the account balance; right after creating the account, the balance should be 0. It should also be possible to log out of the account and exit the program.
 *
 * Example
 * The symbol > represents the user input. Notice that it's not a part of the input.
 *
 * 1. Create an account
 * 2. Log into account
 * 0. Exit
 * >1
 *
 * Your card has been created
 * Your card number:
 * 4000004938320895
 * Your card PIN:
 * 6826
 *
 * 1. Create an account
 * 2. Log into account
 * 0. Exit
 * >2
 *
 * Enter your card number:
 * >4000004938320895
 * Enter your PIN:
 * >4444
 *
 * Wrong card number or PIN!
 *
 * 1. Create an account
 * 2. Log into account
 * 0. Exit
 * >2
 *
 * Enter your card number:
 * >4000004938320895
 * Enter your PIN:
 * >6826
 *
 * You have successfully logged in!
 *
 * 1. Balance
 * 2. Log out
 * 0. Exit
 * >1
 *
 * Balance: 0
 *
 * 1. Balance
 * 2. Log out
 * 0. Exit
 * >2
 *
 * You have successfully logged out!
 *
 * 1. Create an account
 * 2. Log into account
 * 0. Exit
 * >0
 *
 * Bye!
 */
public class BankingSystem {

    public static void main(String[] args) {
        new MenuManager().signInOrSignUp();
    }
}
