package banking.database;

import banking.account.Account;
import banking.account.Balance;
import banking.account.Customer;
import banking.card.Card;
import banking.card.CardNumber;
import banking.card.PinCode;
import banking.sql.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public final class DataBase {

    private final String DATA_BASE_URL;

    public DataBase(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Incorrect database name!");
        }

        String dataBaseFolder = "src\\main\\resources\\";
        String dataBaseAddress = dataBaseFolder + name;
        String jdbc = "jdbc:sqlite:";

        DATA_BASE_URL = jdbc +dataBaseAddress;
    }

    public final void insert(final Account account) {
        if (account == null) {
            throw new IllegalArgumentException();
        }

        try (Connection connection = DriverManager.getConnection(DATA_BASE_URL)) {
            if (connection == null) {
                throw new SQLException();
            }

            long id = Long.parseLong(account.getId());
            String number = account.getCard().getCardNumber().toString();
            String pin = account.getCard().getPinCode().toString();
            long balance = account.getBalance().get();
            String firstName = account.getCustomer().getFirstName();
            String secondName = account.getCustomer().getSecondName();
            String lastName = account.getCustomer().getLastName();
            LocalDate birthDate = account.getCustomer().getBirthDate();

            final String cardQuery = new InsertSqlQuery()
                    .into("card")
                    .fields("id", "number", "pin", "balance")
                    .values("?, ?, ?, ?")
                    .create();
            final String customerQuery = new InsertSqlQuery()
                    .into("customer")
                    .fields("id", "first_name", "second_name", "last_name", "birth_date")
                    .values("?, ?, ?, ?, ?")
                    .create();

            try (PreparedStatement cardStatement = connection.prepareStatement(cardQuery);
                    PreparedStatement customerStatement = connection.prepareStatement(customerQuery)) {
                cardStatement.setObject(1, id);
                cardStatement.setObject(2, number);
                cardStatement.setObject(3, pin);
                cardStatement.setObject(4, balance);

                cardStatement.executeUpdate();

                customerStatement.setObject(1, id);
                customerStatement.setObject(2, firstName);
                customerStatement.setObject(3, secondName);
                customerStatement.setObject(4, lastName);
                customerStatement.setObject(5, birthDate.toString());

                customerStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Connection failed!", exception);
        }
    }

    public final void delete(final Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Incorrect argument");
        }

        try (Connection connection = DriverManager.getConnection(DATA_BASE_URL)) {
            if (connection == null) {
                throw new SQLException();
            }

            final String cardQuery = new DeleteSqlQuery()
                    .from("card")
                    .where("id = ?")
                    .create();
            final String customerQuery = new DeleteSqlQuery()
                    .from("customer")
                    .where("id = ?")
                    .create();

            try (PreparedStatement cardStatement = connection.prepareStatement(cardQuery);
                    PreparedStatement customerStatement = connection.prepareStatement(customerQuery)){
                cardStatement.setObject(1, Long.parseLong(account.getId()));
                cardStatement.executeUpdate();

                customerStatement.setObject(1, Long.parseLong(account.getId()));
                customerStatement.executeUpdate();
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public final Optional<Account> getAccount(final Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        Optional<Account> currentAccount = getAccount(account.getId());

        if (currentAccount.isEmpty() || !currentAccount.get().getCard().getPinCode().equals(account.getCard().getPinCode())) {
            return Optional.empty();
        }

        return currentAccount;
    }

    public final Optional<Account> getAccount(final CardNumber cardNumber) {
        if (cardNumber == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return getAccount(cardNumber.getAccountId());
    }

    public final Optional<Account> getAccount(final String accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        Optional<Account> result = Optional.empty();

        try {
            long id = Long.parseLong(accountId);

            try (Connection connection = DriverManager.getConnection(DATA_BASE_URL)) {
                if(connection == null) {
                    throw new SQLException();
                }

                String cardQuery = new SelectSqlQuery()
                        .select("*")
                        .from("card")
                        .where(String.format("id = %d", id))
                        .create();
                String customerQuery = new SelectSqlQuery()
                        .select("*")
                        .from("customer")
                        .where(String.format("id = %d", id))
                        .create();

                try (Statement cardStatement = connection.createStatement();
                        Statement customerStatement = connection.createStatement();
                        ResultSet cardResultSet = cardStatement.executeQuery(cardQuery);
                        ResultSet customerResultSet = customerStatement.executeQuery(customerQuery)) {
                    long currentId;
                    String number;
                    String pin;
                    long balance;
                    String firstName;
                    String secondName;
                    String lastName;
                    LocalDate birthDate;

                    while (cardResultSet.next()) {
                        currentId = cardResultSet.getLong("id");
                        number = cardResultSet.getString("number");
                        pin = cardResultSet.getString("pin");
                        balance = cardResultSet.getLong("balance");
                        Card card = new Card(new CardNumber(number, true), new PinCode(pin));

                        firstName = customerResultSet.getString("first_name");
                        secondName = customerResultSet.getString("second_name");
                        lastName = customerResultSet.getString("last_name");
                        birthDate = LocalDate.parse(customerResultSet.getString("birth_date"));

                        Customer customer = new Customer(firstName, secondName, lastName, birthDate);

                        if (id == currentId) {
                            result = Optional.of(new Account(customer, card, new Balance(balance)));
                        }
                    }
                } catch (Exception exception) {
                    throw new IllegalStateException(exception);
                }
            } catch (SQLException exception) {
                System.err.println("Connection's failed!");
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Incorrect argument!", exception);
        }

        return result;
    }

    public final void update(final String tableName, final Account accountToUpdate, final String fieldName, final Object newValue) {
        if (accountToUpdate == null || tableName == null || fieldName == null || newValue == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        try (Connection connection = DriverManager.getConnection(DATA_BASE_URL)) {
            final String query = new UpdateSqlQuery()
                    .table(tableName)
                    .set(String.format("%s = %s", fieldName, newValue))
                    .where(String.format("id = %d", Long.parseLong(accountToUpdate.getId())))
                    .create();

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        } catch (SQLException exception) {
            System.err.println("Connection's failed");
        }
    }

    public final boolean transfer(final Account from, final Account to, final long sum) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        if (sum < 0) {
            throw new IllegalArgumentException("Transfer sum must be more than 0!");
        }

        final Optional<Account> currentFrom = getAccount(from.getId());

        if (currentFrom.isEmpty()) {
            throw new IllegalStateException("Something wrong!");
        }

        final Optional<Account> currentTo = getAccount(to.getId());

        if (currentTo.isEmpty()) {
            throw new IllegalArgumentException("Such a card does not exist.");
        }

        try (Connection connection = DriverManager.getConnection(DATA_BASE_URL)) {
            connection.setAutoCommit(false);

            final String query = new UpdateSqlQuery()
                    .table("card")
                    .set("balance = ?")
                    .where("id = ?")
                    .create();

            try (PreparedStatement fromOperation = connection.prepareStatement(query);
                    PreparedStatement toOperation = connection.prepareStatement(query)){
                fromOperation.setObject(1, from.getBalance().get() - sum);
                fromOperation.setObject(2, Long.parseLong(from.getId()));
                fromOperation.executeUpdate();

                toOperation.setObject(1, to.getBalance().get() + sum);
                toOperation.setObject(2, Long.parseLong(to.getId()));
                toOperation.executeUpdate();

                connection.commit();
                return true;
            }
        } catch (SQLException exception) {
            System.err.println("Connection's failed!");
        }

        return false;
    }

    public final boolean containsCardNumber(final CardNumber cardNumber) {
        if (cardNumber == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        return containsCardNumberAsString(cardNumber.getCardNumber());
    }

    private boolean containsCardNumberAsString(final String cardNumber) {
        if (cardNumber == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        int size = 0;

        try (final Connection connection = DriverManager.getConnection(DATA_BASE_URL)) {
            if (connection == null) {
                throw new IllegalStateException("Connection is invalid!");
            }

            final String query = new SelectSqlQuery()
                    .select("*")
                    .from("card")
                    .where(String.format("number LIKE '%s'", cardNumber))
                    .create();

            try (final Statement statement = connection.createStatement();
                 final ResultSet resultSet = statement.executeQuery(query)) {
                size = size(resultSet);
            } catch (SQLException exception) {
                System.err.println(exception.getMessage());
            }
        } catch (SQLException | IllegalStateException exception) {
            System.err.println(exception.getMessage());
        }

        return size != 0;
    }

    public final boolean contains(final Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        String accountId = "";
        String pin = "";

        try (final Connection connection = DriverManager.getConnection(DATA_BASE_URL)) {
            if (connection == null) {
                throw new IllegalStateException("Connection is invalid!");
            }

            final String query = new SelectSqlQuery()
                    .select("*")
                    .from("card")
                    .where(String.format("id = %s", account.getId()))
                    .create();

            try (final Statement statement = connection.createStatement();
                 final ResultSet resultSet = statement.executeQuery(query)) {
                if (!resultSet.next()) {
                    return false;
                }

                accountId = resultSet.getString("id");
                pin = resultSet.getString("pin");
            } catch (SQLException exception) {
                System.err.println(exception.getMessage());
            }
        } catch (SQLException | IllegalStateException exception) {
            System.err.println(exception.getMessage());
        }

        return accountId.equals(account.getId()) && pin.equals(account.getCard().getPinCode().getPin());
    }

    private int size(final ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        int count = 0;

        while (resultSet.next()) {
            count++;
        }

        return count;
    }
}
