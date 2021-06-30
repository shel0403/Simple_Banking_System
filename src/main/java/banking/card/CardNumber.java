package banking.card;

import java.util.Objects;

public class CardNumber {

    private final String cardNumber;

    public CardNumber(final String cardNumber, final boolean isDefault) {
        if (!Cards.correctCardNumber(cardNumber, isDefault)) {
            throw new IllegalArgumentException("Incorrect card number!");
        }

        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public final String getCardNumberWithoutCheckDigit() {
        return cardNumber.substring(0, cardNumber.length() - 1);
    }

    public final String getMii() {
        return cardNumber.substring(0, 1);
    }

    public final String getBin() {
        return cardNumber.substring(0, 6);
    }

    public final String getAccountId() {
        return cardNumber.substring(6, cardNumber.length() - 1);
    }

    public final String getCheckDigit() {
        return cardNumber.substring(cardNumber.length() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardNumber that = (CardNumber) o;
        return getCardNumber().equals(that.getCardNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardNumber());
    }

    @Override
    public String toString() {
        return cardNumber;
    }
}
