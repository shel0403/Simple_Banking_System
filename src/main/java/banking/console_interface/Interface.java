package banking.console_interface;

import banking.account.Account;
import banking.account.Balance;
import banking.account.Customer;
import banking.card.Card;
import banking.card.CardNumber;
import banking.card.Cards;
import banking.card.PinCode;
import banking.database.DataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Optional;

public final class Interface {

    private static final Customer DEFAULT_CUSTOMER
            = new Customer("Denis", "Shelest", LocalDate.of(1984, Month.MARCH, 4));
    private static boolean exit = false;
    private static boolean loggedIn = false;
    private static Optional<Account> currentAccount = Optional.empty();

    private Interface() {}

    public static void mainMenuProcess(final DataBase dataBase, final BufferedReader scanner) {
        if (dataBase == null || scanner == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        do {
            displayMainMenu();
            displayInputGreeting();

            final MainMenu action;

            try {
                action = getMainMenuAction(Integer.parseInt(scanner.readLine()));
            } catch (IOException exception) {
                throw new IllegalStateException("Incorrect input!", exception);
            }

            switch (action) {
                case CREATE:
                    createAccount(dataBase, scanner);
                    break;
                case LOG_IN:
                    login(dataBase, scanner);
                    if (loggedIn) {
                        loginMenuProcess(dataBase, scanner);
                    }
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    System.out.println("Unknown operation!");
                    break;
            }
        } while (!exit);
    }

    public static void loginMenuProcess(final DataBase dataBase, final BufferedReader scanner) {
        if (dataBase == null || scanner == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        do {
            displayLoginMenu();
            displayInputGreeting();

            final LoginMenu action;

            try {
                action = getLoginMenuAction(Integer.parseInt(scanner.readLine()));
            } catch (IOException exception) {
                throw new IllegalStateException("Incorrect input!", exception);
            }

            switch (action) {
                case BALANCE:
                    balance();
                    break;
                case ADD_INCOME:
                    addIncome(dataBase, scanner);
                    break;
                case DO_TRANSFER:
                    doTransfer(dataBase, scanner);
                    break;
                case CLOSE_ACCOUNT:
                    closeAccount(dataBase);
                    break;
                case LOG_OUT:
                    logOut();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    System.out.println("Unknown operation!");
                    break;
            }
        } while (loggedIn);
    }

    public static void displayMainMenu() {
        System.out.println();
        Arrays.stream(MainMenu.values()).forEach(System.out::println);
    }

    private static void displayLoginMenu() {
        System.out.println();
        Arrays.stream(LoginMenu.values()).forEach(System.out::println);
    }

    public static MainMenu getMainMenuAction(final int menuPosition) {
        try {
            return menuPosition == 0 ? MainMenu.EXIT : MainMenu.values()[menuPosition - 1];
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new IllegalArgumentException("Incorrect argument!", exception);
        }
    }

    public static LoginMenu getLoginMenuAction(final int menuPosition) {
        try {
            return menuPosition == 0 ? LoginMenu.EXIT : LoginMenu.values()[menuPosition - 1];
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new IllegalArgumentException("Incorrect argument!", exception);
        }
    }

    public static void createAccount(final DataBase dataBase, final BufferedReader scanner) {
        if (dataBase == null || scanner == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        CardNumber cardNumber;
        PinCode pinCode = Cards.generateRandomPin();

        do {
            cardNumber = Cards.generateRandomCardNumber(true);
        } while (dataBase.containsCardNumber(cardNumber));

        System.out.println("Input your first name:");
        displayInputGreeting();
        String firstName;

        try {
            firstName = scanner.readLine();
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        System.out.println("Input your second name (optional):");
        displayInputGreeting();
        String secondName;

        try {
            secondName = scanner.readLine();
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        System.out.println("Input your last name:");
        displayInputGreeting();
        String lastName;

        try {
            lastName = scanner.readLine();
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        System.out.println("Input your birth date:");
        displayInputGreeting();
        int date;

        try {
            date = Integer.parseInt(scanner.readLine());
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        System.out.println("Input your birth month:");
        displayInputGreeting();
        int month;

        try {
            month = Integer.parseInt(scanner.readLine());
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        System.out.println("Input your birth year:");
        displayInputGreeting();
        int year;

        try {
            year = Integer.parseInt(scanner.readLine());
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }

        LocalDate birthDate = LocalDate.of(year, month, date);
        Customer customer = new Customer(firstName, secondName, lastName, birthDate);

        dataBase.insert(new Account(customer, new Card(cardNumber, pinCode), new Balance()));

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pinCode);
    }

    public static void login(final DataBase dataBase, final BufferedReader scanner) {
        if (dataBase == null || scanner == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        try {
            System.out.println("Enter your card number:");
            displayInputGreeting();

            final String inputCardNumber = scanner.readLine();

            System.out.println("Enter your PIN:");
            displayInputGreeting();

            final String inputPinCode = scanner.readLine();
            final CardNumber cardNumber = new CardNumber(inputCardNumber, true);
            final PinCode pinCode = new PinCode(inputPinCode);
            final Account account = new Account(DEFAULT_CUSTOMER, new Card(cardNumber, pinCode), new Balance());

            currentAccount = dataBase.getAccount(account);

            if (currentAccount.isPresent()) {
                System.out.println("You have successfully logged in!");

                loggedIn = true;
            } else {
                System.out.println("Wrong card number or PIN!");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Wrong card number or PIN!");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void balance() {
        currentAccount.ifPresent(account -> System.out.printf("Balance: %d%n", account.getBalance().get()));
    }

    public static void addIncome(final DataBase dataBase, final BufferedReader scanner) {
        if (dataBase == null || scanner == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        System.out.println("Enter income:");
        displayInputGreeting();

        final long income;

        try {
            income = Long.parseLong(scanner.readLine());
        } catch (IOException exception) {
            throw new IllegalStateException("Incorrect input!", exception);
        }

        if (income < 0) {
            throw new IllegalStateException("Income must be equals or more than 0!");
        }

        if (currentAccount.isPresent()) {
            currentAccount.get().getBalance().increase(income);
            dataBase.update("card", currentAccount.get(), "balance", currentAccount.get().getBalance().get());
            System.out.println("Income was added!");
        }
    }

    public static void doTransfer(final DataBase dataBase, final BufferedReader scanner) {
        if (dataBase == null || scanner == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        System.out.println("Transfer");
        System.out.println("Enter card number:");
        displayInputGreeting();

        final String cardNumber;

        try {
            cardNumber = scanner.readLine();
        } catch (IOException exception) {
            throw new IllegalStateException("Incorrect input!", exception);
        }

        if(!Cards.correctByLoonAlgorithm(new CardNumber(cardNumber, false))) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }

        if (!dataBase.containsCardNumber(new CardNumber(cardNumber, true))) {
            System.out.println("Such a card does not exist.");
            return;
        }

            final Optional<Account> to = dataBase.getAccount(new CardNumber(cardNumber, true));

        if (to.isPresent() && currentAccount.isPresent()) {
            System.out.println("Enter how much money you want to transfer:");
            displayInputGreeting();

            final long sum;

            try {
                sum = Long.parseLong(scanner.readLine());
            } catch (IOException exception) {
                throw new IllegalStateException("Incorrect input!", exception);
            }

            try {
                transfer(dataBase, currentAccount.get(), to.get(), sum);
                System.out.println("Success!");
            } catch (IllegalStateException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private static void transfer(final DataBase dataBase, final Account from, final Account to, final long sum) {
        if (dataBase == null || from == null || to == null) {
            throw new IllegalArgumentException("Incorrect argument");
        }

        if (sum < 0) {
            throw new IllegalArgumentException("Sum must be more than 0!");
        }

        if (from.getBalance().get() < sum) {
            throw new IllegalStateException("Not enough money!");
        }

        if (dataBase.transfer(from, to, sum)) {
            from.getBalance().decrease(sum);
            to.getBalance().increase(sum);
        }
    }

    public static void closeAccount(final DataBase dataBase) {
        if (dataBase == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        currentAccount.ifPresentOrElse(dataBase::delete, () -> {throw new IllegalStateException();});
        loggedIn = false;

        System.out.println("The account has been closed!");
    }

    public static void logOut() {
        System.out.println("You have successfully logged out!");

        currentAccount = Optional.empty();
        loggedIn = false;
    }

    public static void exit() {
        currentAccount = Optional.empty();

        System.out.println("Bye!");

        if (loggedIn) {
            loggedIn = false;
        }

        exit = true;
    }

    public static void displayInputGreeting() {
        System.out.print(">");
    }
}
