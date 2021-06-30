package banking.console_interface;

public enum MainMenu {
    CREATE("1. Create an account"),
    LOG_IN("2. Log into account"),
    EXIT("0. Exit");

    private final String display;

    MainMenu(final String display) {
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
