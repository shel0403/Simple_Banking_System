package banking.account;

import banking.Utility;
import banking.card.Card;

import java.util.Objects;

public class Account {

    private final String id;
    private final Customer customer;
    private final Card card;
    private final Balance balance;

    public Account(final Customer customer, final Card card, final Balance balance) {
        Utility.checkIfCorrect(customer, card, balance);

        this.id = card.getCardNumber().getAccountId();
        this.customer = customer;
        this.card = card;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Card getCard() {
        return card;
    }

    public Balance getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return getId().equals(account.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", customer=" + customer +
                ", card=" + card +
                ", balance=" + balance +
                '}';
    }
}
