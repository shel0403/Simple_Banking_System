package banking.card;

import banking.Utility;

import java.util.Objects;

public class Card {

    private final CardNumber cardNumber;
    private final PinCode pinCode;

    public Card(CardNumber cardNumber, PinCode pinCode) {
        Utility.checkIfCorrect(cardNumber, pinCode);

        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
    }

    public CardNumber getCardNumber() {
        return cardNumber;
    }

    public PinCode getPinCode() {
        return pinCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getCardNumber().equals(card.getCardNumber()) && getPinCode().equals(card.getPinCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardNumber(), getPinCode());
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber=" + cardNumber +
                ", pinCode=" + pinCode +
                '}';
    }
}
