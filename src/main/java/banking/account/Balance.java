package banking.account;

import java.util.Objects;

public class Balance {

    private long balance;

    public Balance() {}

    public Balance(long balance) {
        this.balance = balance;
    }

    public final long get() {
        return balance;
    }

    public final void increase(final long sum) {
        if (sum <= 0) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        balance += sum;
    }

    public final void decrease(final long sum) {
        if(sum <= 0) {
            throw new IllegalArgumentException("Incorrect argument!");
        }

        if (sum > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance -= sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance1 = (Balance) o;
        return balance == balance1.balance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance);
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balance=" + balance +
                '}';
    }
}
