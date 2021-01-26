package banking.dashboard;
import banking.model.AccountManagerImpl;
import banking.model.Constant;

public class MenuManager implements DashBoardManager {

    @Override
    public void displayMainMenu(){
        System.out.println("1. Create an account\n" +
                           "2. Log into account\n" +
                           "0. Exit");
    }

    @Override
    public void displayAccountMenu() {
        System.out.println("1. Balance\n"+
                    "2. Add income\n"    +
                    "3. Do transfer\n"   +
                    "4. Close account\n" +
                    "5. Log out\n"       +
                    "0. Exit");
    }

    @Override
    public void signInOrSignUp() {
        AccountManagerImpl accountManager = new AccountManagerImpl();
        displayMainMenu();
        boolean exit = false;
        do {
            String userChoice = Constant.SCANNER.next();
            switch (userChoice){
                case "1":
                    accountManager.createAccount();
                    displayMainMenu();
                    break;
                case "2":
                    accountManager.logIntoAccount();
                    if(AccountManagerImpl.getEndProgram()){
                        exit = true;
                    } else {
                        displayMainMenu();
                    }
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
}
