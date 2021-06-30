package banking;

public final class Utility {

    private Utility(){}

    public static void checkIfCorrect(Object... args) {
        if (args == null) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        for (Object arg: args) {
            if (arg == null) {
                throw new IllegalArgumentException("Incorrect argument!");
            }
        }
    }
}
