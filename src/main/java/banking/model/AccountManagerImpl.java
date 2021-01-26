package banking.model;

import banking.dashboard.MenuManager;
import banking.dao.DatabaseConnectorService;
import banking.luhnAlgorithm.LuhnAlgorithm;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class AccountManagerImpl implements AccountManager{
    TreeMap<Long, Integer> accountList = new TreeMap<>();
    public static Boolean endProgram = false;

    public static Boolean getEndProgram() {
        return endProgram;
    }

    public static void setEndProgram(Boolean endProgram) {
        AccountManagerImpl.endProgram = endProgram;
    }

    @Override
    public void createAccount() {
        Random random = new Random();

        //Account Identifier Number
        String accountIdentifier = "";
        while ("".equals(accountIdentifier)){
            int cardPart1 = random.nextInt(99999);
            int cardPart2 = random.nextInt(99999);
            accountIdentifier = cardPart1+""+cardPart2;
            if(accountIdentifier.length() != 10 || !LuhnAlgorithm.checkLuhnAlgorithm(accountIdentifier)){
                accountIdentifier = "";
            }
        }

        //Pin Number
        String pinNumber = "";
        while (pinNumber.isEmpty()){
            int pin = random.nextInt(9999);
            pinNumber = String.valueOf(pin);
            if(pinNumber.length() != 4){
                pinNumber = "";
            }
        }

        //BIN (Bank Identification Number) + Account Identifier Number
        CreditCardHandler<String, String> account = new CreditCardHandler<>(Constant.ISSUER_IDENTIFICATION_NUMBER+accountIdentifier, pinNumber);

        DatabaseConnectorService dbConnector = new DatabaseConnectorService();
        dbConnector.insert(account.getCreditCardNumber(), account.getCreditCardPin(), 0);

        System.out.println("\nYour card has been created\n" +
                "Your card number:\n"            +
                account.getCreditCardNumber()    +
                "\nYour card PIN:\n"             +
                account.getCreditCardPin()       +
                "\n");
    }

    @Override
    public void logIntoAccount() {
        //Store all the created account.
        DatabaseConnectorService dbConnector = new DatabaseConnectorService();
        dbConnector.selectAll();
        accountList.putAll(DatabaseConnectorService.getCreditCardList());

        System.out.println("Enter your card number:");
        long cardNumberLogIn = Constant.SCANNER.nextLong();
        System.out.println("Enter your PIN:");
        int pinLogIn = Constant.SCANNER.nextInt();

        String response = "";
        for (Map.Entry<Long, Integer> entry : accountList.entrySet()) {
            if (cardNumberLogIn == entry.getKey() && pinLogIn == entry.getValue()){
                response = "\nYou have successfully logged in!\n";
                break;
            } else {
                response = "\nWrong card number or PIN!\n";
            }
        }
        System.out.println(response);

        MenuManager menu = new MenuManager();
        if ("\nYou have successfully logged in!\n".equals(response)){
            menu.displayAccountMenu();
            boolean exit = false;
            do {
                int currentBalance = dbConnector.getBalance(String.valueOf(cardNumberLogIn), String.valueOf(pinLogIn));

                String userChoice = Constant.SCANNER.next();
                switch (userChoice){
                    case "1":
                        System.out.println("\nBalance: "+currentBalance+"\n");
                        menu.displayAccountMenu();
                        break;
                    case "2":
                        System.out.println("\nEnter income:");
                        int newBalance = Constant.SCANNER.nextInt();
                        dbConnector.addIncome(String.valueOf(cardNumberLogIn), String.valueOf(pinLogIn), currentBalance, newBalance);
                        menu.displayAccountMenu();
                        break;
                    case "3":
                        System.out.println("\nTransfer\n Enter card number:");
                        Long creditCardNumber = Constant.SCANNER.nextLong();
                        if (accountList.containsKey(creditCardNumber)){
                            if (creditCardNumber.equals(cardNumberLogIn)){
                                System.out.println("You can't transfer money to the same account!\n");
                            } else {
                                System.out.println("Enter how much money you want to transfer:");
                                int amountToTransfer = Constant.SCANNER.nextInt();

                                if (currentBalance < amountToTransfer) {
                                    System.out.println("Not enough money!\n");
                                } else {
                                    int accountReceiverBalance = dbConnector.getAccountReceiverBalance(String.valueOf(creditCardNumber));
                                    dbConnector.doTransfert(String.valueOf(cardNumberLogIn), String.valueOf(creditCardNumber), currentBalance, amountToTransfer, accountReceiverBalance);
                                    System.out.println("Success!\n");
                                }
                            }
                        } else if (!LuhnAlgorithm.checkLuhnAlgorithm(String.valueOf(creditCardNumber))){
                            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
                        } else if(!accountList.containsKey(creditCardNumber)){
                            System.out.println("Such a card does not exist.\n");
                        }
                        menu.displayAccountMenu();
                        break;
                    case "4":
                        dbConnector.closeAccount(String.valueOf(cardNumberLogIn));
                        accountList.clear(); // Clear the accountList because we'll always fill him at every log in attempt.
                        System.out.println("\nThe account has been closed!\n");
                        exit = true;
                        break;
                    case "5":
                        System.out.println("\nYou have successfully logged out!\n");
                        exit = true;
                        accountList.clear(); // Clear the accountList because we'll always fill him at every log in attempt.
                        break;
                    case "0":
                        System.out.print("\nBye!\n");
                        exit = true;
                        accountList.clear(); // Clear the accountList because we'll always fill him at every log in attempt.
                        dbConnector.closeDatabaseConnexion();
                        setEndProgram(true); // Will help to kill the program by skipping the main menu.
                        break;
                    default:
                        System.out.println("Unknown Command, choose between option 0, 1 & 2");
                        menu.displayMainMenu();
                }
            }while (!exit);
        }
    }
}
