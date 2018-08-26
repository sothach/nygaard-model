package org.seefin.nygaard.model.identifiers;

import org.seefin.nygaard.model.parties.Organization;

/**
 * ISO7812 value object definition (Identification card number)<p/>
 * ISO/IEC 7812 Identification cards: Identification of issuers, first published by the International
 * Organization for Standardization (ISO) in 1989, is the international standard that specifies:
 * "a numbering system for the identification of issuers of cards that require an issuer identification
 * number (IIN) to operate in international, inter-industry and/or intra-industry interchange",
 * and procedures for registering IINs<p/>
 * Format:
 * <pre>
 *     1     2-6                 7->        last
 *    +-----+-------------------+-----------+-------+
 *    | MII | issuer identifier |           | check |
 *    +-----+-------------------+ account # | digit |
 *    | issuer identification # |           |       |
 *    +-------------------------+-----------+-------+
 *    |         ISO 7812 identification number      |
 *    +---------------------------------------------+
 * </pre>
 * Usages:
 * Bank card Numbers (e.g., CreditCard)
 * SIM Card ICC-ID (Integrated Circuit Card ID) - encodes information differently:
 * <p/>
 * Refactor: The ICCID (Integrated Circuit Card Identifier) identifies each SIM internationally. A full ICCID is 19 or 20 characters.
 * It is possible to extract the ICCID by using the 'AT!ICCID?' modem command.
 * The format of the ICCID is: MMCC IINN NNNN NNNN NN C x
 * <dl>
 * <dt>MM</dt><dd>Constant (ISO 7812 Major Industry Identifier, = 89 for
 * "Telecommunications administrations and private operating agencies")</dd>
 * <dt>CC</dt><dd>Country Code (i.e. 61 = Australia, 86 = China)</dd>
 * <dt>II</dt><dd>Issuer Identifier (AAPT = 14, EZI-PhoneCard = 88, Hutchison = 06, Optus = 02/12/21/23,
 * Telstra = 01, Telstra Business = 00/61/62, Vodafone = 03)</dd>
 * <dt>N{12}</dt><dd>Account ID ("SIM number")</dd>
 * <dt>C</dt><dd>Checksum calculated from the other 19 digits using the Luhn algorithm.</dd>
 * <dt>x</dt><dd>An extra 20th digit is returned by the 'AT!ICCID?' command, but doesn't
 * seem to be an official part of the ICCID.</dd>
 * </dl>
 *
 * @author phillipsr
 */
public final class ISO7812
        extends AccountNumber {
    private final long value;

    /**
     * Constructor to create a new ISO7812 number<p/>
     * <b>Note</b> ISO7812 values created via this constructor are validated
     * only in terms of the number's checksum; to perform full validation,
     * that the card issuer is valid and the number length is correct, use
     * one of the static factory methods defined in this class
     *
     * @param CardNumber representation of the number
     */
    public ISO7812(long CardNumber) {
        if (LuhnChecksum.isValid(Long.toString(CardNumber)) == false) {
            throw new InvalidISO7812Exception(Long.toString(CardNumber), "CardNumber checksum invalid");
        }
        this.value = CardNumber;
    }

    /**
     * Validating factory method
     *
     * @param string to initialize ISO8583 from
     * @return a ISO7812 value object equivalent to the string supplied
     * @throws InvalidISO7812Exception if the string does not represent an ISO7812
     *                                 object conforming to a registered scheme (see {@link ISO7812Scheme.java})
     */
    public static ISO7812
    parse(String string) {
        return ISO7812Scheme.parse(string);
    }

    /**
     * Validating factory method
     *
     * @param number representing an ISO7812 number
     * @return an ISO7812 number initialized from the supplied number
     * @throws InvalidISO7812Exception if the number does not represent an ISO7812
     *                                 object conforming to a registered scheme (see {@link ISO7812Scheme.java})
     */
    public static ISO7812
    valueOf(long number) {
        return ISO7812Scheme.parse(Long.toString(number));
    }

    /**
     * @return the initial digit of this number, representing the Major Industry Identifier (MII)
     */
    public int
    getMajorIndustryIdentifier() {
        return getIssuerIdentifier().getMajorIndustryIdentifier();
    }

    /**
     * @return the initial segment of this number, representing the Issuer Identifier (II)
     */
    public BIN
    getIssuerIdentifier() {
        return new BIN((int) (value / getFactor()));
    }

    /**
     * @return the organization registered as the issuer of this card number
     */
    public Organization
    getIssuer() {
        return ISO7812Scheme.getScheme(getIssuerIdentifier()).getIssuer();
    }

    /**
     * @return the factor to separate the issuer identifier portion from the account number
     */
    private int
    getFactor() {
        return (int) Math.pow(10, getAccountNumberLength() + 1);
    }

    /**
     * @return the account number portion of this number
     */
    public int
    getAccountNumber() {
        return (int) (value
                - (getIssuerIdentifier().intValue() * getFactor())) / 10;
    }

    /**
     * @return the name of the major industry for this card number
     */
    public String
    getMajorIndustryName() {
        return ISO7812Scheme.getMajorIndustryName(this);
    }

    /**
     * @return the length of the account number portion of this number
     */
    private int getAccountNumberLength() {
        return (int) (Math.log10(value) + 1) - 8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String
    externalForm() {
        return Long.toString(value);
    }

    /**
     * @return an obfusticated version of this number, suitable for printing in logs etc.
     */
    @Override
    public String
    toString() {
        return String.format("%7d%" + getAccountNumberLength() + "s%d",
                value / getFactor(), "***************", value % 10);
    }

    /**
     * @return the Long integer equivalent of this number
     */
    public Long longValue() {
        return value;
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
        return this.value == ((ISO7812) other).value;
    }

    @Override
    public int
    hashCode() {
        return this.longValue().hashCode();
    }

    @Override
    public int
    compareTo(AccountNumber other) {
        if (other.getClass() == this.getClass()) {
            return Long.compare(this.value, ((ISO7812) other).value);
        }
        return super.compareTo(other);
    }

    /**
     * @param cardNumber to be validated
     * @return true if the supplied string represents a valid ISO7812 number,
     * in terms of its checksum value
     */
    public static boolean
    isValidChecksum(String cardNumber) {
        return LuhnChecksum.isValid(cardNumber);
    }

}
