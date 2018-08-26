package org.seefin.nygaard.model.instruments;

import java.util.Currency;

import org.seefin.nygaard.model.financial.Account;
import org.seefin.nygaard.model.identifiers.ISO7812;
import org.seefin.nygaard.model.parties.Party;


/**
 * Generic model of a payment card, representing the aspects
 * common to all card types (Credit, Debit, ..., Starbucks)
 *
 * @author phillipsr
 */
public abstract class PaymentCard
        extends Account {
    protected PaymentCard(Party owner, Currency cardCurrency, ISO7812 cardNumber) {
        super(owner, cardNumber.getIssuer(), cardCurrency, cardNumber);
    }

    /**
     * Does <code>number</code> represent a valid credit card number?
     *
     * @param number
     * @return true if <code>number</code> represent a valid credit card number
     */
    public static boolean
    isValid(String number) {
        return ISO7812.isValidChecksum(number);
    }
}
