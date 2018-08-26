package org.seefin.nygaard.model.financial;

import org.seefin.nygaard.model.identifiers.GUID;
import org.seefin.nygaard.model.identifiers.Identity;


/**
 * This request bean represents the normalized concept of a credit transfer request
 *
 * @author phillipsr
 */
public class FinancialTransaction
        implements Transaction {
    private final MonetaryAmount amount;
    private final Account debitAccount;
    private final Account creditAccount;
    private Identity identity = GUID.createUniqueId();

    /**
     * Create a new credit transfer request
     *
     * @param debitAccount  from which the funds will be drawn
     * @param amount        to be transferred
     * @param creditAccount to which the funds will be credited
     * @throws IllegalArgumentException if any of the parameters are null,
     *                                  or if the two accounts are the same
     */
    public FinancialTransaction(Account debitAccount,
                                MonetaryAmount amount, Account creditAccount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount may not be null");
        }
        if (debitAccount == null) {
            throw new IllegalArgumentException("Debit Account may not be null");
        }
        if (creditAccount == null) {
            throw new IllegalArgumentException("Credit Account may not be null");
        }
        if (debitAccount.getIdentifier().equals(creditAccount.getIdentifier())) {
            throw new IllegalArgumentException("Cannot transfer to/from the same account (" + debitAccount + ")");
        }
        this.amount = amount;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public Account getDebitInstrument() {
        return debitAccount;
    }

    public Account getCreditInstrument() {
        return creditAccount;
    }

    @Override
    public String
    toString() {
        return this.getClass().getSimpleName()
                + "{id=" + identity + ", Amount: " + amount
                + ", Payment Instrument: " + debitAccount
                + ", Recipient Instrument: " + creditAccount + "}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity getIdentity() {
        return identity;
    }

}
