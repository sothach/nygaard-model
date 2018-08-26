package org.seefin.nygaard.model.instruments;

import org.junit.Assert;
import org.junit.Test;
import org.seefin.nygaard.model.identifiers.ISO7812;
import org.seefin.nygaard.model.identifiers.InvalidISO7812Exception;
import org.seefin.nygaard.model.identifiers.MSISDN;
import org.seefin.nygaard.model.identifiers.PersonalName;
import org.seefin.nygaard.model.parties.Individual;

import java.util.Currency;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;


public class TestCreditCard {
    private static final Individual TEST_CUSTOMER
            = new Individual(new PersonalName(new String[]{"Joe", "Blochs"}), MSISDN.parse("+353863567279"));
    protected static final Currency EURO_CURRENCY = Currency.getInstance("EUR");

    private static final String VALID_CC_NUMBER = "4552-7204-1234-5677";
    private static final String VALID_CC_NUMBER2 = "6338-4502-6568-9767";
    private static final String INVALID_CC_NUMBER = "6338-6502-6568-9762";
    private static final String BADCHECKSUM_CC_NUMBER = "6338-4502-6568-9762";

    @Test
    public void
    testCreditCardValidation() {
        CreditCard card = new CreditCard(TEST_CUSTOMER, EURO_CURRENCY, ISO7812.parse(VALID_CC_NUMBER));
        assertThat("4552720412345677", equalTo(card.getIdentifier().externalForm()));
    }

    @Test
    public void
    testCreditCardEquality() {
        CreditCard card = new CreditCard(TEST_CUSTOMER, EURO_CURRENCY, ISO7812.parse(VALID_CC_NUMBER2));
        assertThat("6338450265689767", equalTo(card.getIdentifier().externalForm()));
        Assert.assertTrue(ISO7812.class == card.getIdentifier().getClass());
        ISO7812 cardId = (ISO7812) card.getIdentifier();
        assertThat(6338450265689767L, equalTo(cardId.longValue()));
    }

    @Test
    public void
    testInequality() {
        ISO7812 card1 = ISO7812.parse(VALID_CC_NUMBER);
        ISO7812 card2 = ISO7812.parse(VALID_CC_NUMBER2);
        assertThat(card1, not(equalTo(card2)));
    }

    @Test(expected = InvalidISO7812Exception.class)
    public void
    testInvalidCreditCardValidation() {
        try {
            ISO7812.parse(INVALID_CC_NUMBER);
        } catch (InvalidISO7812Exception e) {
            Assert.assertEquals("No scheme registered", e.getReason());
            throw e;
        }
    }

    @Test(expected = InvalidISO7812Exception.class)
    public void
    testInvalidCheckSum() {
        try {
            ISO7812.parse(BADCHECKSUM_CC_NUMBER);
        } catch (InvalidISO7812Exception e) {
            Assert.assertEquals("CardNumber checksum invalid", e.getReason());
            throw e;
        }
    }
}
