package banking;

import banking.dao.DatabaseConnectorService;
import banking.dashboard.MenuManager;

/**
 * Description - Step 3 :
 * It's very upsetting when the data about registered users disappears after the program is completed.
 * To avoid this problem, we need to create a database where you will store
 * all the necessary information about the created credit cards.
 * We will use SQLite to create the database.
 *
 * In this stage, create a database with a table titled <card> with the following columns:
 * id INTEGER
 * number TEXT
 * pin TEXT
 * balance INTEGER DEFAULT 0
 *
 * PS: The database file should be created when the program starts, if it hasn’t yet been created.
 * And all created cards should be stored in the database from now.
 */
public class BankingSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        args = new String [1];
        args[0] = "card.db";
        DatabaseConnectorService.databaseConnector(args[0]);

        DatabaseConnectorService dbConnector = new DatabaseConnectorService();
        dbConnector.createCardTable();
        MenuManager menuManager = new MenuManager();
        menuManager.signInOrSignUp();
    }
}
