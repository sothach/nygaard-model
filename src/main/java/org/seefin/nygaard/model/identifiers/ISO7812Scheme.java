package org.seefin.nygaard.model.identifiers;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.seefin.nygaard.model.parties.Organization;


/**
 * Factory capable of creating ISO7812 (Credit Card) identification numbers
 * <p/>
 * The factory is initialized from a set of scheme definitions, in a property
 * file, located by the system property: <code>ISO7812.scheme.specification</code>
 * The property file should contain entries defining the accepted card number types,
 * defined, for example, as follows:
 * <pre>
 * 55=MasterCard;16
 * </pre>
 * This specifies that MasterCard numbers should start with the issuer identifier '55',
 * and the total number length must be 15 digits
 * <p/>
 * This is the 'companion class' (public) that Josh Bloch recommends when working with
 * immutable value objects, to perform factory type operations, such as value construction
 *
 * @author roy.phillips
 */
public class ISO7812Scheme
        implements Serializable {
    private final static String[] MIIMap = new String[]
            {
                    /* 0 */ "ISO/TC 68 and other industry assignments",
                    /* 1 */ "Airlines",
                    /* 2 */ "Airlines and other industry assignments",
                    /* 3 */ "Travel and entertainment",
                    /* 4 */ "Banking and financial",
                    /* 5 */ "Banking and financial",
                    /* 6 */ "Merchandizing and banking",
                    /* 7 */ "Petroleum",
                    /* 8 */ "Telecommunications and other industry assignments",
                    /* 9 */ "National assignment"
            };
    private static final Pattern NON_NUMERICS = Pattern.compile("[^0-9]");

    // ... loads ISO7812 specifications from file named by this property
    private static final String ISO7812_PROPERTIES_KEY = "ISO7812.scheme.specification";
    private static final String ISO7812_PROPERTIES_DEFAULT = "org/seefin/nygaard/model/identifiers/ISO7812Scheme.properties";
    private static final Map<Integer, ISO7812Scheme> issuerMap = loadSchemeDefinitions();

    public static final int MinCreditCardLengthLength = 13;
    public static final int MaxCreditCardLengthLength = 19;
    private static final String LENGTH_ERROR = "CardNumber string must be between "
            + MinCreditCardLengthLength + " and " + MaxCreditCardLengthLength + " in length";

    // per-scheme instance values:
    private final Organization issuer;
    private final Set<Integer> validLengths;

    /**
     * Constructor called from the static scheme loader to instantiate a scheme
     * from the parsed specification, as represented by the parameters below
     *
     * @param bin      Bank Identification Number: unique Id of the scheme (key)
     * @param issuer   organization issuing the number (and associated card)
     * @param ilengths valid lengths for numbers in this scheme
     */
    private ISO7812Scheme(Organization issuer, Integer[] ilengths) {
        this.issuer = issuer;
        this.validLengths = new HashSet<>(Arrays.asList(ilengths));
    }

    /**
     * Convert the supplied ISO7812 number string into a ISO7812 value object, if it
     * is valid and conforms to a registered scheme<br/> (to bypass registered scheme validation.
     * call the <code>new ISO7812(number)</code> constructor directly)
     *
     * @param cardNumber candidate for conversion
     * @return an ISO7812 value object equivalent to the supplied parameter
     * @throws IllegalArgumentException if the cardNumber is null
     * @throws InvalidISO7812Exception  if the supplied string is badly-formed or
     *                                  the Issuer ID is not registered with this factory
     */
    public static ISO7812
    parse(String cardNumber) {
        if (cardNumber == null) {
            throw new IllegalArgumentException("CardNumber string cannot be null");
        }
        String digits = NON_NUMERICS.matcher(cardNumber).replaceAll("");
        int length = digits.length();
        if (length < MinCreditCardLengthLength || length > MaxCreditCardLengthLength) {
            throw new InvalidISO7812Exception(cardNumber, LENGTH_ERROR);
        }

        BIN bin = new BIN(Integer.parseInt(digits.substring(0, 7)));
        ISO7812Scheme scheme = getScheme(bin);
        if (scheme.getIssuer().getCommonName().isEmpty()) {
            throw new InvalidISO7812Exception(cardNumber, "No scheme registered", bin.toString());
        }

        if (scheme.validLengths.contains(length) == false) {
            throw new InvalidISO7812Exception(cardNumber,
                    "CardNumber length must one of: " + scheme.validLengths);
        }

        return new ISO7812(Long.parseLong(digits)); // checksum will be validated in constructor
    }

    /**
     * @return the issuer owning this scheme
     */
    Organization getIssuer() {
        return issuer;
    }

    /**
     * Look-up the scheme describing the supplied issuer code, by
     * attempting to find the largest matching key in the set of
     * registered schemes<p/>
     * ISO7812 issuer Ids are hierarchical, so this lookup incrementally
     * walks back up that hierarchy until it finds a match
     *
     * @param bin to be looked-up (includes major industry identifier prefix)
     * @return the matching scheme, otherwise UNKNOWN_ORGANIZATION to indicate scheme not found
     */
    static ISO7812Scheme
    getScheme(BIN bin) {
        ISO7812Scheme result = null;
        int issuerIdentifier = bin.intValue();
        while (issuerIdentifier > 0 && result == null) {
            result = issuerMap.get(issuerIdentifier);
            issuerIdentifier /= 10;
        }
        // if no issuer registered, create an entry based on the supplied BIN
        if (result == null) {
            result = new ISO7812Scheme(new Organization(bin, ""), new Integer[]{});
            issuerMap.put(bin.intValue(), result);
        }
        return result;
    }

    /**
     * @param number
     * @return the name/description of the major industry sector of the card number issuer
     */
    static String
    getMajorIndustryName(ISO7812 number) {
        return MIIMap[number.getIssuerIdentifier().getMajorIndustryIdentifier()];
    }

    /**
     * @return a string describing this scheme, with issuer Id, issuer name and valid length
     */
    @Override
    public String
    toString() {
        return issuer.getId() + " Issuer: " + issuer.getCommonName() + ", Valid lengths: " + validLengths;
    }

    /**
     * Create a ISO7812Scheme from the specification passed in string form
     * <p/>
     * Example specification:
     * <pre>
     *   "4=VISA;13,16"
     * </pre>
     * defines an ISO7812 scheme where the issuer code is "4", the issuer name is "VISA",
     * and valid lengths are 13 and 16
     *
     * @param specification string describing the scheme
     * @param schemeName    identifying the scheme
     * @return a new scheme initialized from the supplied specification
     */
    private static ISO7812Scheme
    parseSpecification(final BIN issuerId, final String specification) {
        final String[] spec = specification.split(";");
        assert spec.length == 2 : "part rules for issuer and number lengths specified";
        // get the issuer:
        Organization issuer = getOrganization(spec[0], issuerId);
        // get the list of valid card lengths for this issuer:
        List<String> lengths = Arrays.asList(spec[1].split(","));
        Integer[] ilengths = new Integer[lengths.size()];
        int i = 0;
        for (String length : lengths) {
            ilengths[i++] = Integer.parseInt(length.trim());
        }
        return new ISO7812Scheme(issuer, ilengths);
    }

    /**
     * @param issuerDetails string containing issuer name, and optionally,
     *                      the issuer BIC code, separated by a comma
     * @param issuerId      the six digit BIN part of the code (e.g., MII + II)
     * @return an organization object, built the issuer details supplied;
     * if a BIC was provided, the Organization ID is set to that,
     * otherwise, a generic ReferenceCode based on the BIN is used
     */
    private static Organization
    getOrganization(String issuerDetails, BIN issuerId) {
        String[] parts = issuerDetails.split(",");
        return new Organization(
                parts.length > 1 ? ISO9362.valueOf(parts[1]) : issuerId,
                parts[0]);
    }

    /**
     * Iterate over the schemes in the supplied properties map, parsing each
     * definition and storing it against the scheme key in the resulting map
     *
     * @param schemes defined as property key/value pairs
     * @return map of scheme keys (BIN code) to issuer scheme definitions
     */
    private static Map<Integer, ISO7812Scheme>
    getSchemeMap(Properties schemes) {
        assert schemes.size() > 0;
        Map<Integer, ISO7812Scheme> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : schemes.entrySet()) {
            int issuerId = Integer.parseInt((String) entry.getKey());
            ISO7812Scheme scheme
                    = ISO7812Scheme.parseSpecification(new BIN(issuerId), (String) entry.getValue());
            result.put(issuerId, scheme);
        }
        assert result.size() == schemes.size() : "all schemes registered";

        return result;
    }

    /**
     * Load the ISO7812 scheme definitions from a properties file specified
     * by the system property <code>ISO7812_PROPERTIES_KEY</code>
     *
     * @return map of schemes, indexed by (possibly partial) BIN number
     * @throws RuntimeException
     */
    private static Map<Integer, ISO7812Scheme>
    loadSchemeDefinitions() {
        String ISO7812Formats = System.getProperty(ISO7812_PROPERTIES_KEY, ISO7812_PROPERTIES_DEFAULT);
        return getSchemeMap(PropertyLoader.getProperties(ISO7812Formats));
    }

    /**
     * Clear the currently registered ISO7812 schemes in this singleton, and reload
     * the scheme definitions from the resource supplied<br/>
     *
     * @param schemeResource location (e.g., filename, URL) of ISO7812 scheme definitions
     */
    public static void
    loadScheme(String schemeResource) {
        issuerMap.clear();
        issuerMap.putAll(getSchemeMap(PropertyLoader.getProperties(schemeResource)));
    }

    /**
     * Reset the ISO7812 scheme configuration to its default value
     */
    public static void
    resetScheme() {
        loadScheme(ISO7812_PROPERTIES_DEFAULT);
    }

}