package banking.dao;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.TreeMap;

public class DatabaseConnectorService {
    public static TreeMap<Long, Integer> creditCardList = new TreeMap<>();
    private static String databaseURL;

    public static String getDatabaseURL() {
        return databaseURL;
    }

    public static void setDatabaseURL(String databaseURL) {
        DatabaseConnectorService.databaseURL = databaseURL;
    }

    public static TreeMap<Long, Integer> getCreditCardList() {
        return creditCardList;
    }

    public static void setCreditCardList(TreeMap<Long, Integer> creditCardList) {
        DatabaseConnectorService.creditCardList = creditCardList;
    }

    /**
     * Connect to a database (Only used in this class).
     */
    private Connection databaseConnector() {
        // SQLite connection string
        String url = getDatabaseURL();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Create or Connect to a database
     *
     * @param fileName the database file name
     */
    public static void databaseConnector(String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName;

        setDatabaseURL(url);

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            con.isValid(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a Credit card table if not exit in the db.s3db database.
     */
    public void createCardTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT NOT NULL,\n"
                + "	balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = this.databaseConnector();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the card table
     *
     * @param number is the Credit card number.
     * @param pin is the pin associated to the credit card.
     * @param balance the amount available in the credit card.
     */
    public void insert(String number, String pin, int balance) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES(?,?,?)";

        try (Connection conn = this.databaseConnector();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, number);
            statement.setString(2, pin);
            statement.setInt(3, balance);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Select all rows into the card table
     */
    public void selectAll() {

        try (Connection con = this.databaseConnector()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {

                try (ResultSet creditCard = statement.executeQuery("SELECT * FROM card")) {
                    while (creditCard.next()) {
                        // Retrieve column values
                        String number = creditCard.getString("number");
                        String pin = creditCard.getString("pin");

                        /**
                         * Crucial part where we will store all the created Credit Card and use it later on
                         * in the MenuManager class when we'll try to connect into an account.
                         * @param number for the Credit Card number.
                         * @param pin for the pin number.
                         */
                        creditCardList.put(Long.parseLong(number), Integer.parseInt(pin));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the balance available for a specific credit card.
     *
     * @param number is the Credit card number.
     * @param pin is the pin associated to the credit card.
     */
    public int getBalance(String number, String pin) {
        String sql = "SELECT balance FROM card WHERE number = ? AND pin = ?";
        int balance = 0;
        try (Connection con = this.databaseConnector();
             PreparedStatement statement = con.prepareStatement(sql)) {
            // Set the values.
            statement.setString(1, number);
            statement.setString(2, pin);

            ResultSet result = statement.executeQuery();

            balance = result.getInt("balance");

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return balance;
    }

    /**
     * Display the balance available for a specific credit card.
     * @param number is the Credit card number.
     */
    public int getAccountReceiverBalance(String number) {
        String sql = "SELECT balance FROM card WHERE number = ?";
        int balance = 0;
        try (Connection con = this.databaseConnector();
             PreparedStatement statement = con.prepareStatement(sql)) {
            // Set the values.
            statement.setString(1, number);
            ResultSet result = statement.executeQuery();

            balance = result.getInt("balance");

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return balance;
    }

    /**
     * Delete a specific credit card from the Database.
     * @param number is the Credit card number.
     */
    public void closeAccount(String number) {
        String deleteAccount = "DELETE FROM card WHERE number = ?";
        try (Connection con = this.databaseConnector();
             PreparedStatement statementDeleteAccount = con.prepareStatement(deleteAccount)) {

            // Set the values.
            statementDeleteAccount.setString(1, number);
            statementDeleteAccount.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add income to a specific account.
     *
     * @param number is the Credit card number.
     * @param pin is the pin associated to the credit card.
     * @param currentBalance is the current balance link to the credit card.
     * @param newBalance which is the Updated balance related to the account.
     */
    public void addIncome(String number, String pin, int currentBalance, int newBalance) {
        String updateIncome = "UPDATE card SET balance = ? WHERE number = ? AND pin = ?";
        Connection con = null;
        try {
            con = this.databaseConnector();
            PreparedStatement statementIncomeUpdated = con.prepareStatement(updateIncome);

            // Disable auto-commit mode
            con.setAutoCommit(false);

            // Set the new income.
            statementIncomeUpdated.setInt(1, currentBalance + newBalance);
            statementIncomeUpdated.setString(2, number);
            statementIncomeUpdated.setString(3, pin);
            statementIncomeUpdated.executeUpdate();

            con.commit();
            System.out.println("Income was added!\n");

        } catch (SQLException e) {
            try {
                System.err.print("Transaction is being rolled back...");
                con.rollback();
            } catch (SQLException excep) {
                excep.printStackTrace();
            }
            System.out.println(e.getMessage());
        }
    }

    /**
     * Do a money transfer from one account to another.
     *
     * @param accountSender is the Credit card sender number.
     * @param accountReceiver is the Credit card receiver number.
     * @param accountSenderBalance is the sender balance in his credit card.
     * @param accountReceiverBalance is the receiver balance in his credit card.
     * @param amountToTransfer is the amount the sender is willing to transfer.
     */
    public void doTransfert(String accountSender, String accountReceiver, int accountSenderBalance, int amountToTransfer, int accountReceiverBalance) {
        String updateAccountSenderBalance = "UPDATE card SET balance = ? WHERE number = ?";
        String updateAccountReceiverBalance = "UPDATE card SET balance = ? WHERE number = ?";

        Connection con = null;
        try {
            con = this.databaseConnector();
            PreparedStatement statementUpdateAccountSenderBalance = con.prepareStatement(updateAccountSenderBalance);
            PreparedStatement statementUpdateAccountReceiverBalance = con.prepareStatement(updateAccountReceiverBalance);


            // Disable auto-commit mode
            con.setAutoCommit(false);

            // Set the new sender balance.
            statementUpdateAccountSenderBalance.setInt(1, accountSenderBalance - amountToTransfer);
            statementUpdateAccountSenderBalance.setString(2, accountSender);
            statementUpdateAccountSenderBalance.executeUpdate();

            // Set the new receiver balance.
            statementUpdateAccountReceiverBalance.setInt(1, accountReceiverBalance + amountToTransfer);
            statementUpdateAccountReceiverBalance.setString(2, accountReceiver);
            statementUpdateAccountReceiverBalance.executeUpdate();

            con.commit();

        } catch (SQLException e) {
            try {
                System.err.print("Transaction is being rolled back...");
                con.rollback();
            } catch (SQLException excep) {
                excep.printStackTrace();
            }
            System.out.println(e.getMessage());
        }
    }

    /**
     * Close Database connexion.
     */
    public void closeDatabaseConnexion() {
        Connection conn;
        try {
            conn = this.databaseConnector();
            if (conn != null) {
                conn.close();
                if (conn.isClosed()){
                    System.out.println("Database Closed...");
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
