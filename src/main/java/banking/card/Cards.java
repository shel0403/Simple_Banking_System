package banking.card;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public final class Cards {

    public static final int MIN_BIN_LENGTH = 4;
    public static final int MAX_BIN_LENGTH = 6;
    public static final int DEFAULT_BIN_LENGTH = 6;
    public static final int MIN_ACCOUNT_LENGTH = 9;
    public static final int MAX_ACCOUNT_LENGTH = 12;
    public static final int DEFAULT_ACCOUNT_LENGTH = 9;
    public static final int PIN_LENGTH = 4;

    public static final String DEFAULT_BIN;

    static {
        final StringBuilder defaultBin = new StringBuilder("4");

        IntStream.range(0, DEFAULT_BIN_LENGTH - 1)
                .forEach(i -> defaultBin.append(0));

        DEFAULT_BIN = defaultBin.toString();
    }

    private static final Pattern CORRECT_CARD_NUMBER = Pattern.compile(String.format(
            "[1-9]\\d{%d,%d}\\d{%d,%d}\\d",
            MIN_BIN_LENGTH - 1, MAX_BIN_LENGTH - 1,
            MIN_ACCOUNT_LENGTH, MAX_ACCOUNT_LENGTH
    ));
    private static final Pattern CORRECT_DEFAULT_CARD_NUMBER = Pattern.compile(String.format(
            "%s\\d{%d}\\d",
            DEFAULT_BIN, DEFAULT_ACCOUNT_LENGTH
    ));
    private static final Pattern CORRECT_PIN = Pattern.compile(String.format("\\d{%d}", PIN_LENGTH));

    private Cards(){}

    public static boolean correctByLoonAlgorithm(final CardNumber cardNumber) {
        if (cardNumber == null) {
            return false;
        }

        String checkDigit = cardNumber.getCheckDigit();
        String cardNumberWithoutCheckDigit = cardNumber.getCardNumberWithoutCheckDigit();

        return generateCheckDigit(getDigits(cardNumberWithoutCheckDigit)) == Integer.parseInt(checkDigit);
    }

    public static CardNumber generateRandomCardNumber(final boolean isDefault) {
        return isDefault ? generateRandomDefaultCardNumber() : generateRandomCardNumber();
    }

    private static CardNumber generateRandomDefaultCardNumber() {
        final Random generator = new Random();
        final StringBuilder cardNumber = new StringBuilder(DEFAULT_BIN);

        cardNumber.append(generator.nextInt(9) + 1);

        IntStream.range(1, DEFAULT_ACCOUNT_LENGTH)
                .forEach(i -> cardNumber.append(generator.nextInt(10)));

        cardNumber.append(generateCheckDigit(getDigits(cardNumber.toString())));

        return new CardNumber(cardNumber.toString(), true);
    }

    private static CardNumber generateRandomCardNumber() {
        final Random generator = new Random();
        final int binLength = MIN_BIN_LENGTH - 1
                + generator.nextInt(MAX_BIN_LENGTH - MIN_BIN_LENGTH + 1);
        final int accountIdLength = MIN_ACCOUNT_LENGTH
                + generator.nextInt(MAX_ACCOUNT_LENGTH - MIN_ACCOUNT_LENGTH + 1);
        final StringBuilder cardNumber = new StringBuilder();

        cardNumber.append(1 + generator.nextInt(9));

        IntStream.range(0, binLength)
                .forEach(i -> cardNumber.append(generator.nextInt(10)));

        cardNumber.append(generator.nextInt(9) + 1);

        IntStream.range(1, accountIdLength)
                .forEach(i -> cardNumber.append(generator.nextInt(10)));

        cardNumber.append(generateCheckDigit(getDigits(cardNumber.toString())));

        return new CardNumber(cardNumber.toString(), false);
    }

    public static int generateCheckDigit(final int[] digits) {
        return (sumValues(multiplyByTwoAndSubtractIfMoreThanNineEvenIndexes(digits)) * 9) % 10;
    }

    private static int sumValues(final int[] digits) {
        return Arrays.stream(digits).sum();
    }

    private static int[] multiplyByTwoAndSubtractIfMoreThanNineEvenIndexes(final int[] digits) {
        final int[] result = Arrays.copyOfRange(digits, 0, digits.length);

        for (int i = 0; i < result.length; i += 2) {
            result[i] *= 2;
            result[i] -= (result[i] > 9) ? 9 : 0;
        }

        return result;
    }

    private static int[] getDigits(final String cardNumber) {
        final char[] characters = cardNumber.toCharArray();
        final int[] digits = new int[characters.length];

        for (int i = 0; i < digits.length; i++) {
            digits[i] = Integer.parseInt(String.valueOf(characters[i]));
        }

        return digits;
    }

    public static PinCode generateRandomPin() {
        final Random generator = new Random();
        final StringBuilder pinCode = new StringBuilder();

        for (int i = 0; i < PIN_LENGTH; i++) {
            pinCode.append(generator.nextInt(10));
        }

        return new PinCode(pinCode.toString());
    }

    public static boolean correctPin(final String pin) {
        return CORRECT_PIN.matcher(pin).matches();
    }

    public static boolean correctCardNumber(final String cardNumber, final boolean isDefault) {
        return isDefault ? correctDefaultCardNumber(cardNumber) : correctCardNumber(cardNumber);
    }

    private static boolean correctCardNumber(final String cardNumber) {
        return CORRECT_CARD_NUMBER.matcher(cardNumber).matches();
    }

    private static boolean correctDefaultCardNumber(final String cardNumber) {
        return CORRECT_DEFAULT_CARD_NUMBER.matcher(cardNumber).matches();
    }

    // FIXME: 6/29/2021 DELETE!!!
    public static void main(String[] args) {
        CardNumber cardNumber = new CardNumber("2000007269641764", false);
        System.out.println(correctByLoonAlgorithm(cardNumber));
    }
}
