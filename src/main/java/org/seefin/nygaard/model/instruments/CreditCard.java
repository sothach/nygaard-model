package org.seefin.nygaard.model.instruments;

import java.util.Currency;

import org.seefin.nygaard.model.identifiers.ISO7812;
import org.seefin.nygaard.model.parties.Party;


public final class CreditCard
        extends PaymentCard {
    public CreditCard(Party owner, Currency cardCurrency, ISO7812 cardNumber) {
        super(owner, cardCurrency, cardNumber);
    }

}
