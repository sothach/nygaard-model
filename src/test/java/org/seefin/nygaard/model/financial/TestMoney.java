package org.seefin.nygaard.model.financial;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class TestMoney {
    @Test
    public void
    testRounding() {
        // So 4.505 -> 4.51, 5.505 -> 5.50
        MonetaryAmount amount = MonetaryAmount.parse("EUR 4.545");
        Assert.assertEquals("EUR4.54", amount.toString());

        amount = MonetaryAmount.parse("EUR 4.555");
        Assert.assertEquals("EUR4.56", amount.toString());
    }

    @Test
    public void
    testMonetaryPrecision() {
        MonetaryAmount amount = MonetaryAmount.parse("EUR 10");
        Assert.assertEquals("EUR10.00", amount.toString());
        Assert.assertEquals(BigDecimal.TEN.setScale(2), amount.getBigDecimal());

        amount = MonetaryAmount.parse("EUR 10.5");
        Assert.assertEquals("EUR10.50", amount.toString());
        Assert.assertEquals(new BigDecimal("10.50"), amount.getBigDecimal());

        amount = MonetaryAmount.parse("EUR 20.615");
        Assert.assertEquals("EUR20.62", amount.toString());
        Assert.assertEquals(new BigDecimal("20.62"), amount.getBigDecimal());

        amount = MonetaryAmount.parse("EUR 30.525");
        Assert.assertEquals("EUR30.52", amount.toString());
        Assert.assertEquals(new BigDecimal("30.52"), amount.getBigDecimal());

        amount = MonetaryAmount.parse("EUR 10.506");
        Assert.assertEquals("EUR10.51", amount.toString());
        Assert.assertEquals(new BigDecimal("10.51"), amount.getBigDecimal());
    }

    @Test
    public void
    testStringParse() {
        MonetaryAmount amount = MonetaryAmount.parse("EUR 10.5");
        Assert.assertEquals("EUR10.50", amount.toString());
        Assert.assertEquals(new BigDecimal("10.50"), amount.getBigDecimal());
    }

    @Test
    public void testSubtractionSuccess() {
        MonetaryAmount hundredEuro = MonetaryAmount.parse("EUR 100");
        MonetaryAmount fiftyEuro = MonetaryAmount.parse("EUR 50");
        MonetaryAmount result = hundredEuro.subtract(fiftyEuro);
        Assert.assertEquals(result, fiftyEuro);
    }

    @Test
    public void testAddition() {
        MonetaryAmount hunderedEuro = MonetaryAmount.parse("EUR 100");
        MonetaryAmount fiftyEuro = MonetaryAmount.parse("EUR -50.00");
        MonetaryAmount result = hunderedEuro.add(fiftyEuro);
        Assert.assertEquals(MonetaryAmount.parse("EUR50"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionDifferentCurrenciesFail1() {
        MonetaryAmount hunderedEuro = MonetaryAmount.parse("EUR 100");
        MonetaryAmount zeroDollars = MonetaryAmount.parse("USD 0");
        hunderedEuro.subtract(zeroDollars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionDifferentCurrenciesFailure() {
        MonetaryAmount hunderedEuro = MonetaryAmount.parse("EUR100");
        MonetaryAmount tenDollars = MonetaryAmount.parse("USD10");
        tenDollars.subtract(hunderedEuro);
    }

    @Test
    public void testDivision() {
        MonetaryAmount value = MonetaryAmount.parse("EUR17");
        value = value.divide(MonetaryAmount.parse("EUR3"));
        System.out.println(value);
        Assert.assertEquals(MonetaryAmount.parse("EUR5.67"), value);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivisionByZero() {
        MonetaryAmount value = MonetaryAmount.parse("EUR17");
        value = value.divide(MonetaryAmount.parse("EUR0"));
    }

    @Test
    public void testMultiply() {
        MonetaryAmount value = MonetaryAmount.parse("EUR1");
        value = value.multiply(MonetaryAmount.parse("EUR3"));
        System.out.println(value);
        Assert.assertEquals(MonetaryAmount.parse("EUR3"), value);
    }
}
