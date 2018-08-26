package org.seefin.nygaard.model.financial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Representation of a monetary value in a specified currency
 *
 * @author phillipsr
 */
public final class MonetaryAmount
        implements Serializable, Comparable<MonetaryAmount> {
    /* This implementation uses a Long to store the value part of a monetary amount,
     * so max value is: 92,233,720,368,547,758.07 (for a currency with fraction = 2) */
    private final long value;
    private final Currency currency;

    /**
     * Create a monetary amount representing the currency and value supplied
     *
     * @param currency of this amount
     * @param cents    value of the amount
     */
    public MonetaryAmount(Currency currency, long cents) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency must be provided");
        }
        this.currency = currency;
        this.value = cents;
    }

    /**
     * Create a monetary value in the specified currency
     *
     * @param currency
     * @param value
     */
    public static MonetaryAmount
    fromBigDecimal(Currency currency, BigDecimal value) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        value = value.setScale(
                currency.getDefaultFractionDigits(), java.math.RoundingMode.HALF_DOWN);
        BigDecimal factor = BigDecimal.valueOf((long) Math.pow(10, currency.getDefaultFractionDigits()));
        long longValue = value.multiply(factor).longValue();
        return new MonetaryAmount(currency, longValue);
    }

    /**
     * Initialize a new Monetary value from the string supplied,
     * a concatenation of the the ISO 4217 currency code and
     * a decimal value string, e.g.:
     * <p/>
     * "EUR10" or "USD 230.50"
     *
     * @param currencyAndValue of format CCC9... (ISO4217 code plus number)
     */
    public static MonetaryAmount
    parse(String currencyAndValue) {
        if (currencyAndValue == null) {
            throw new IllegalArgumentException("currencyAndValue string cannot be null");
        }
        currencyAndValue = currencyAndValue.replaceAll(" ", "");
        if (currencyAndValue.length() < 4) {
            throw new IllegalArgumentException(
                    "Invalid currencyAndValue string: should be CCC9... (ISO4217 code plus number)");
        }

        Currency cc = Currency.getInstance(currencyAndValue.substring(0, 3));
        long longValue = roundHalfEven(cc, new BigDecimal(currencyAndValue.substring(3)));

        return new MonetaryAmount(cc, longValue);
    }

    private static long
    roundHalfEven(Currency cc, BigDecimal value) {
        // apply banker's rounding on the supplied value to the precision required for the currency
        value = value.setScale(
                cc.getDefaultFractionDigits(), java.math.RoundingMode.HALF_EVEN);
        // convert the value to cents
        BigDecimal factor = BigDecimal.valueOf((long) Math.pow(10, cc.getDefaultFractionDigits()));
        return value.multiply(factor).longValue();
    }

    /**
     * Create a monetary value from a double value, in the specified currency
     *
     * @param currency
     * @param value
     */
    public MonetaryAmount(Currency currency, double value) {
        this(currency, (long) (value * Math.pow(10, currency.getDefaultFractionDigits())));
    }


    /**
     * @return the value part converted to a BigDecimal
     */
    public BigDecimal
    getBigDecimal() {
        boolean isNegative = value < 0;
        BigDecimal result = BigDecimal.valueOf(Math.abs(value)).divide(
                new BigDecimal((int) Math.pow(10, currency.getDefaultFractionDigits())));
        if (isNegative == true) {
            result = result.negate();
        }
        return result.setScale(currency.getDefaultFractionDigits());
    }

    public Currency getCurrency() {
        return currency;
    }

    public MonetaryAmount
    add(MonetaryAmount augend) {
        if (augend.getCurrency() != currency) {
            throw new IllegalArgumentException(
                    "Cannot add Monetary Amounts in difference currencies ("
                            + this.currency + " & " + augend.getCurrency() + ")");
        }

        return new MonetaryAmount(currency, value + augend.value);
    }

    public MonetaryAmount
    subtract(MonetaryAmount monetaryAmount) {
        if (monetaryAmount.getCurrency() != currency) {
            throw new IllegalArgumentException(
                    "Cannot subtract Monetary Amounts in different currencies ("
                            + this.currency + " & " + monetaryAmount.getCurrency() + ")");
        }
        return new MonetaryAmount(this.getCurrency(), value - monetaryAmount.value);
    }

    public MonetaryAmount
    negate() {
        return new MonetaryAmount(this.getCurrency(), -value);
    }

    /**
     * @return true if the current amount is zero
     */
    public boolean isZero() {
        return value == 0;
    }

    public MonetaryAmount
    multiply(MonetaryAmount other) {
        if (other.getCurrency() != currency) {
            throw new IllegalArgumentException(
                    "Cannot subtract Monetary Amounts in different currencies ("
                            + this.currency + " & " + other.getCurrency() + ")");
        }
        if (other.value == 0) {
            throw new IllegalArgumentException("Divide by zero attempt");
        }
        int centFactor = (int) Math.pow(10, currency.getDefaultFractionDigits());
        long result = Math.round(((double) value * (double) other.value) / centFactor);
        return new MonetaryAmount(this.getCurrency(), result);
    }


    /**
     * @param parse
     * @return
     */
    public MonetaryAmount
    divide(MonetaryAmount other) {
        if (other.getCurrency() != currency) {
            throw new IllegalArgumentException(
                    "Cannot subtract Monetary Amounts in different currencies ("
                            + this.currency + " & " + other.getCurrency() + ")");
        }
        if (other.value == 0) {
            throw new ArithmeticException("/ by zero");
        }
        int centFactor = (int) Math.pow(10, currency.getDefaultFractionDigits());
        long result = Math.round(((double) value / (double) other.value) * centFactor);
        return new MonetaryAmount(this.getCurrency(), result);
    }

    @Override
    public String
    toString() {
        return currency.getCurrencyCode() + getBigDecimal();
    }

    @Override
    public boolean
    equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        MonetaryAmount test = (MonetaryAmount) other;
        if (test.getCurrency() != currency) {
            throw new IllegalArgumentException(
                    "Cannot test equality between Monetary Amounts in different currencies ("
                            + this.currency + " & " + test.getCurrency() + ")");
        }

        return currency == test.currency && value == test.value;
    }

    @Override
    public int
    hashCode() {
        int result = 7;
        result = 31 * result + (int) value;
        result = 31 * result + currency.toString().hashCode();
        return result;
    }

    /**
     * Absolute value implementation.
     *
     * @return
     */
    public MonetaryAmount
    abs() {
        return new MonetaryAmount(this.getCurrency(), Math.abs(value));
    }

    @Override
    public int
    compareTo(MonetaryAmount other) {
        if (other.getCurrency() != currency) {
            throw new IllegalArgumentException(
                    "Cannot compare Monetary Amounts in different currencies ("
                            + this.currency + " & " + other.getCurrency() + ")");
        }
        return Long.compare(this.value, other.value);
    }

}