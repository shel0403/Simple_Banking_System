package banking.card;

import java.util.Objects;

public class PinCode {

    private final String pin;

    public PinCode(final String pin) {
        if (!Cards.correctPin(pin)) {
            throw new IllegalArgumentException("Incorrect PIN-code");
        }

        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PinCode pinCode = (PinCode) o;
        return getPin().equals(pinCode.getPin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPin());
    }

    @Override
    public String toString() {
        return pin;
    }
}
