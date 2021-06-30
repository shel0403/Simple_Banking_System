package banking;

import banking.console_interface.Interface;
import banking.database.DataBase;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {
            DataBase dataBase = new DataBase(args[1]);

            try (BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in))) {
                Interface.mainMenuProcess(dataBase, scanner);
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.err.println("Incorrect command line arguments!");
        }
    }
}
