package banking.console_interface;

public enum LoginMenu {
    BALANCE("1. Balance"),
    ADD_INCOME("2. Add income"),
    DO_TRANSFER("3. Do transfer"),
    CLOSE_ACCOUNT("4. Close account"),
    LOG_OUT("5. Log out"),
    EXIT("0. Exit");

    private final String display;

    LoginMenu(final String display) {
        if (display == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
