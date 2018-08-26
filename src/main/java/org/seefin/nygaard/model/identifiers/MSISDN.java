package org.seefin.nygaard.model.identifiers;

/**
 * MSISDN ("Mobile Subscriber Integrated Services Digital Network", (alternate: Mobile Station ISDN)
 * is a number uniquely identifying a subscription in a GSM or a UMTS mobile network, the telephone
 * number of the SIM card in a mobile phone.
 * <p/>
 * The MSISDN together with IMSI [2] are two important numbers used for identifying a mobile subscriber.
 * The latter identifies the SIM, i.e. the card inserted in to the mobile phone, while the former is
 * used for routing calls to the subscriber.  IMSI is often used as a key in the HLR ("subscriber
 * database") and MSISDN is the number normally dialled to connect a  call to the mobile phone.
 * A SIMis uniquely associated to an IMSI, while the MSISDN can change in time (e.g. due to number
 * portability),  i.e., different MSISDNs can be associated with the SIM.
 * <p/>
 * The MSISDN follows the numbering plan defined in the ITU-T recommendation E.164.
 * <p/>
 * <dl>
 * <dt>MSISDN</dt><dd>CC + NDC + SN</dd>
 * <dt>CC</dt><dd>Country Code</dd>
 * <dt>NDC</dt><dd>National Destination Code, identifies one or part of a PLMN[2]</dd>
 * <dt>SN</dt><dd>Subscriber Number</dd>
 * </dl>
 * [1] <i>International Mobile Subscriber Identity</i><br/>
 * [2] <i>Public Land Mobile Network</i><p/>
 * <h3>Example</h3>
 * MSISDN: 353521234567
 * <table border="1">
 * <tr><td>CC</td><td>353</td><td>Ireland</td></tr>
 * <tr><td>NDC</td><td>52</td><td>Waterford</td></tr>
 * <tr><td>SN</td><td>1234567</td><td>Subscriber's number</td></tr>
 * </table>
 * <p/>
 *
 * @author roy.phillips
 */
public final class MSISDN
        implements Comparable<MSISDN>, Identity {
    private final long value;

    private transient MSISDNScheme scheme;

    /**
     * Instantiate a new MSIDN having the value set from the supplied number,
     * inferring the scheme by matching configured country codes against the
     * start of the number
     *
     * @param number long integer representation of a MSISDN
     * @throws IllegalArgumentException is number does not belong to a configured scheme
     */
    public MSISDN(Long number) {
        value = number;
        scheme = getScheme();
        if (scheme == null) {
            throw new IllegalStateException(
                    "unable to determine scheme from number (" + number + ")");
        }
    }

    /**
     * Instantiate a new MSIDN from the supplied number string,
     * inferring the scheme by matching configured country codes against the
     * start of the number
     *
     * @param number String representation of a MSISDN
     * @throws IllegalArgumentException is number does not belong to a configured scheme
     */
    public MSISDN(String number) {
        this(Long.parseLong(number));
    }

    /**
     * Private constructor, used by factory methods to set-up the value and scheme of
     * a new MSISDN (as they are final fields)
     *
     * @param value  the MSISDN number as a long
     * @param scheme the scheme to which the MSISDN belongs
     */
    private MSISDN(Long value, MSISDNScheme scheme) {
        if (scheme == null) {
            throw new IllegalArgumentException("scheme may not be null");
        }
        this.value = value;
        this.scheme = scheme;
    }

    /**
     * Create a new MSISDN from the three parts supplied, belonging to the
     * specified scheme
     *
     * @param cc     country code
     * @param ndc    national dialling code
     * @param sn     subscriber number
     * @param scheme to which the resultant MSISDN should belong
     * @return a new MSISDN built from the parameters
     */
    static MSISDN
    create(final int cc, final int ndc, final int sn, MSISDNScheme scheme) {
        if (scheme == null) {
            throw new IllegalArgumentException("scheme may not be null");
        }
        return new MSISDN(scheme.longValue(cc, ndc, sn), scheme);
    }

    /**
     * Create a new MSISDN from the three parts supplied, attempting to
     * guess the scheme from the country code and length of the number
     *
     * @param cc  country code
     * @param ndc national dialling code
     * @param sn  subscriber number
     * @return a new MSISDN built from the parameters
     */
    public static MSISDN
    create(final int cc, final int ndc, final int sn) {
        int length = ("" + cc + "" + ndc + "" + sn).length();
        MSISDNScheme scheme = MSISDNScheme.getSchemeForCC(cc, length);
        if (scheme == null) {
            throw new IllegalStateException(
                    "unable to determine scheme from number (" + cc + ndc + sn + ")");
        }
        return MSISDN.create(cc, ndc, sn, scheme);
    }

    /**
     * Create a MSISDN from the string representation supplied,
     * inferring the scheme from the known set of schemes
     *
     * @param msisdnString
     * @return new MSISDN instance initialized from parameter
     * @throws IllegalArgumentException if the string is not a valid MSISDN for known schemes
     */
    public static MSISDN
    parse(String msisdnString) {
        return MSISDNScheme.createMSISDN(msisdnString);
    }

    /**
     * Create a new MSIDN having the value set from the supplied
     * number, and inferring the scheme by matching declared
     * country codes against the start of the number
     *
     * @param number
     * @return
     */
    public static MSISDN
    valueOf(long number) {
        return MSISDNScheme.fromLong(number);
    }

    /**
     * Answer with the country code part of the MSISDN number
     *
     * @return an integer representing the country code (CC)
     */
    public int
    getCC() {
        return (int) (value / getScheme().ccfactor);
    }

    /**
     * Answer with the national dialing code part of the MSISDN number
     *
     * @return an integer representing the national dialing code (NDC)
     */
    public int
    getNDC() {
        return (int) ((value - (getCC() * getScheme().ccfactor))
                / getScheme().ndcfactor);
    }

    /**
     * Answer with the subscriber number part of the MSISDN number
     *
     * @return an integer representing the subscriber numbe (SN)
     */
    public int
    getSN() {
        return (int) (value - (getCC() * getScheme().ccfactor)
                - (getNDC() * getScheme().ndcfactor));
    }

    /**
     * Return the scheme defining this number, recreating if necessary (e.g.,
     * after de-serialization, as it is transient)
     *
     * @return
     */
    private MSISDNScheme
    getScheme() {
        if (scheme == null) {
            scheme = MSISDNScheme.fromLong(value).scheme;
        }
        return scheme;
    }

    /**
     * Answer with the canonical MSISDN format String for the current number
     */
    @Override
    public String
    externalForm() {
        return "+" + value;
    }

    /**
     * Answer with the simple numeric format String
     */
    @Override
    public String
    toString() {
        return Long.toString(value);
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
        return value == ((MSISDN) other).value;
    }

    @Override
    public int
    hashCode() {
        return Long.valueOf(value).hashCode();
    }

    /**
     * Answer with a long value representing this MSISDN's numeric value
     *
     * @return
     */
    public long
    longValue() {
        return value;
    }

    @Override
    public int
    compareTo(MSISDN other) {
        return Long.compare(value, other.value);
    }

}
