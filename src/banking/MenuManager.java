package banking;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MenuManager implements DashBoardManager {
    TreeMap<Long, Integer> accountList = new TreeMap<>();

    @Override
    public void displayMainMenu(){
        System.out.println("1. Create an account\n" +
                           "2. Log into account\n" +
                           "0. Exit");
    }

    @Override
    public void displayAccountMenu() {
        System.out.println("1. Balance\n" +
                           "2. Log out\n" +
                           "0. Exit");
    }

    @Override
    public void signInOrSignUp() {
        displayMainMenu();
        boolean exit = false;
        do {
            String userChoice = Constant.SCANNER.next();
            switch (userChoice){
                case "1":
                    createAccount();
                    displayMainMenu();
                    break;
                case "2":
                    LogIntoAccount();
                    displayMainMenu();
                    break;
                case "0":
                    System.out.print("\nBye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Unknown Command, choose between option 0, 1 & 2");
                    displayMainMenu();
            }
        }while (!exit);
    }

    public void createAccount() {
        Random random = new Random();
        //Account Number
        String accountNumber = "";
        while ("".equals(accountNumber)){
            int cardPart1 = random.nextInt(99999);
            int cardPart2 = random.nextInt(99999);
            accountNumber = cardPart1+""+cardPart2;
            if(accountNumber.length() != 10){
                accountNumber = "";
            }
        }

        //Pin Number
        String pinNumber = "";
        while ("".equals(pinNumber)){
            int pin = random.nextInt(9999);
            pinNumber = String.valueOf(pin);
            if(pinNumber.length() != 4){
                pinNumber = "";
            }
        }

        CreditCardHandler<Long, Integer> account = new CreditCardHandler<>(Long.parseLong(Constant.ISSUER_IDENTIFICATION_NUMBER+""+Long.parseLong(accountNumber)), Integer.parseInt(pinNumber));

        //Store all the created account.
        accountList.put(account.getCreditCardNumber(), account.getCreditCardPin());

        System.out.println("\nYour card has been created\n" +
                "Your card number:\n"            +
                account.getCreditCardNumber()   +
                "\nYour card PIN:\n"            +
                account.getCreditCardPin()      +
                "\n");
    }

    public void LogIntoAccount() {
        System.out.println("Enter your card number:");
        long cardNumber = Constant.SCANNER.nextLong();
        System.out.println("Enter your PIN:");
        int pin = Constant.SCANNER.nextInt();

        String response = "";
        for (Map.Entry<Long, Integer> entry : accountList.entrySet()) {
            if (cardNumber == entry.getKey() && pin == entry.getValue()){
                response = "\nYou have successfully logged in!\n";
                break;
            } else {
                response = "\nWrong card number or PIN!\n";
            }
        }
        System.out.println(response);

        if ("\nYou have successfully logged in!\n".equals(response)){
            displayAccountMenu();
            boolean exit = false;
            do {
                String userChoice = Constant.SCANNER.next();
                switch (userChoice){
                    case "1":
                        System.out.println("\nBalance: 0\n");
                        displayAccountMenu();
                        break;
                    case "2":
                        System.out.println("\nYou have successfully logged out!\n");
                        exit = true;
                        break;
                    case "0":
                        System.out.print("\nBye!\n");
                        exit = true;
                        break;
                    default:
                        System.out.println("Unknown Command, choose between option 0, 1 & 2");
                        displayMainMenu();
                }
            }while (!exit);
        }
    }
}
