package banking_v0_2;

/**
 * Description - Step 2 :
 * In this stage, we will find out what the purpose
 * of the checksum is and what the Luhn algorithm is used for.
 *
 * The main purpose of the check digit is to verify that the card number is valid.
 * Say you're buying something online, and you type in your credit card number
 * incorrectly by accidentally swapping two digits, which is one of the most common errors.
 *
 * When the website looks at the number you've entered and applies the Luhn algorithm to the first 15 digits,
 * the result won't match the 16th digit on the number you entered.
 *
 * The computer knows the number is invalid,
 * and it knows the number will be rejected if it tries to submit the purchase for approval,
 * so you're asked to re-enter the number.
 *
 * Another purpose of the check digit is to catch clumsy attempts to create fake credit card numbers.
 * Those who are familiar with the Luhn algorithm, however, could get past this particular security measure.
 */
public class BankingSystem {

    public static void main(String[] args) {
        new MenuManager().signInOrSignUp();
    }
}
