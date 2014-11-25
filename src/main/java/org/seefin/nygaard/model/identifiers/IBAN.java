package org.seefin.nygaard.model.identifiers;

import org.seefin.nygaard.model.locations.ISO3166;

import com.google.common.base.Preconditions;


/**  Representation of an International Bank Account Number (IBAN) 
 * <p/>
 *<pre>
 * +--------------------------------------------------+
 * | IBAN                                             |
 * | +---------+------------------------------------+ |
 * | | ISO3166 | BBAN                               | |
 * | |         | +------+--------+----------------+ | |
 * | |         | | BANK | BRANCH | ACCOUNT NUMBER | | |
 * | |         | +------+--------+----------------+ | |
 * | +---------+------------------------------------+ |
 * +--------------------------------------------------+
 * </pre> 
 * The IBAN consists of a ISO 3166-1 alpha-2 country code, followed by two check digits,
 * and up to thirty alphanumeric characters for the domestic bank account number, the 
 * BBAN (Basic Bank Account Number), which itself may be composed of bank code, branch
 * code and account number, as specified by the national scheme (see {@link IBANScheme.java}) */
public final class IBAN extends AccountNumber {
    private final String  value;
    private final ISO3166 countryCode;
    private IBANScheme    scheme;

    /** Create an IBAN from the supplied string
     * 
     * @param value of the IBAN, may contain punctuation
     * @throws IllegalStateException if the supplied value does 
     * 			not represent a valid IBAN code */
    public IBAN(final String code) {
        Preconditions.checkNotNull(code, "IBAN string cannot be null");
        String value = sanitize(code);
        Preconditions.checkArgument(sanitize(code).length() >= 5, "IBAN string must be at least 5 characters long");
        countryCode = ISO3166.valueOf(value.substring(0, 2));
        scheme = IBANScheme.lookupScheme(countryCode);
        if (scheme == null) {
            throw new InvalidIBANException(countryCode, "no scheme defined for country", value);
        }
        if (value.length() != scheme.getLength()) {
            throw new InvalidIBANException(countryCode, "IBAN string must be " + scheme.getLength() + " characters in length", value);
        }
        this.value = value;

        scheme.validate(this);
    }


    /** @return an IBAN parsed the supplied string, 
     *  @param value of the IBAN, may contain punctuation
     *  @throws IllegalArgumentException if the supplied value does 
     * 			not represent a valid IBAN code */
    public static IBAN parse(String value) {
        return new IBAN(value);
    }

    /**
     * Create an IBAN from its component parts
     * 
     * @param countryCode
     * @param bankCode
     * @param branchCode
     * @param accountNumber
     * @return
     */
    public static IBAN create(ISO3166 countryCode, String bankCode, String branchCode, String accountNumber) {
        IBANScheme scheme = IBANScheme.lookupScheme(countryCode);
        if (scheme == null) {
            throw new InvalidIBANException(countryCode, "no scheme defined for country", countryCode.toString());
        }
        return IBANScheme.create(countryCode, bankCode, branchCode, accountNumber);
    }

    /**
     * @return the string representation of this IBAN, obfusticated
     */
    @Override
    public String toString() {
        return obsusticate(value, 5, 2);
    }

    /**
     * @return the BBAN (Basic Bank Account Number) segment of this IBAN
     */
    public BBAN getBBAN() {
        return new BBAN(value.substring(4));
    }

    /** {@inheritDoc} */
    @Override
    public String externalForm() {
        return value;
    }

    /** @return the BankCode segment of this IBAN */
    public String getBankCode() {
        return scheme.getBankCode(value);
    }

    /** @return the BranchCode segment of this IBAN */
    public String getBranchCode() {
        return scheme.getBranchCode(value);
    }

    /** @return the AccountNumber segment of this IBAN */
    public String getAccountNumber() {
        return scheme.getAccountNumber(value);
    }

    /** @return the CountryCode segment of this IBAN */
    public ISO3166 getCountryCode() {
        return countryCode;
    }

}
