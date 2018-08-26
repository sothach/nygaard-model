package org.seefin.nygaard.model.identifiers;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.seefin.nygaard.model.locations.ISO3166;


/**
 * Creates IBAN numbers based on country schemes, that are loaded from a resource
 * named by the system property <code>iban.scheme.specification</code>
 * (e.g., <code>-Diban.scheme.specification=path/to/iban.file</code>)
 * <p/>
 * The file format consists of a unique key (the ISO country code), followed by
 * a specification of the national (BBAN) segment of the number scheme, for example:
 * Ireland has a 22 character IBAN, "IEKK AAAA BBBB BBCC CCCC CC",
 * where A = BIC bank code, B = Bank/branch code (sort code), C = Account number
 * <dl>
 * <dt>a</dt><dd>[A-Z]</dd>
 * <dt>n</dt><dd>[0-9]</dd>
 * <dt>c</dt><dd>[a-zA-Z0-9]</dd>
 * </dl>
 * <pre>
 *   IE:BANK=4c;BRANCH=6n,ACC=8n
 * </pre>
 * This is the 'companion class' (public) that Josh Bloch recommends when working with
 * immutable value objects, to perform factory type operations, such as value construction
 *
 * @author roy.phiilips
 */
public class IBANScheme implements Serializable {
    private enum PartCode {
        BANK,
        BRANCH,
        ACC
    }

    // ... loads IBAN specifications from file named by this property
    private static final String IBAN_PROPERTIES_KEY = "iban.scheme.specification";
    private static final String IBAN_PROPERTIES_DEFAULT = "org/seefin/nygaard/model/identifiers/IBANScheme.properties";
    // get known IBAN schemes from property file
    private static final Map<ISO3166, IBANScheme> schemes = loadSchemeDefinitions();

    // per-scheme instance values:
    private final Map<PartCode, IBANRule> rules;
    private final int length;
    private final ISO3166 countryCode;
    private String errorMessage;

    /**
     * Constructor called from the static scheme loader to instantiate a scheme
     * from the parsed specification, as represented by the parameters below
     *
     * @param cc     unique name of the scheme (key)
     * @param rules  set of rules defining the parts
     * @param length
     * @param length total length of a number in this scheme
     */
    private IBANScheme(final ISO3166 cc, final Map<PartCode, IBANRule> rules, final int length) {
        this.countryCode = cc;
        this.rules = ImmutableMap.copyOf(rules);
        this.length = length;
    }

    /**
     * Create an IBAN instance from the supplier parameters, if conforming
     * to a registered IVBAN scheme
     *
     * @param countryCode   ISO3166 Alpha-2 country code (mandatory)
     * @param bankCode      (mandatory)
     * @param branchCode    (may be null or blank)
     * @param accountNumber (mandatory)
     * @return new IBAN instance initialized from parameters
     * @throws IllegalArgumentException if mandatory parameter is null or blank,
     *                                  or if the
     * @throws InvalidIBANException     if the constructed IBAN does not conform to a registered scheme
     */
    public static IBAN create(final ISO3166 countryCode, final String bankCode, final String branchCode, final String accountNumber) {
        Preconditions.checkNotNull(countryCode, "CountryCode cannot be null");
        Preconditions.checkArgument(bankCode != null && !bankCode.isEmpty(), "BankCode cannot be null/blank");
        Preconditions.checkArgument(accountNumber != null && !accountNumber.isEmpty(), "AccountNumber cannot be null/blank");
        final String translated = translateChars(bankCode + branchCode + accountNumber + countryCode + "00");
        final int checksum = 98 - modulo97(translated);
        return new IBAN(String.format("%s%02d%s%s%s", countryCode.toString(), checksum, bankCode, branchCode != null ? branchCode : "",
                accountNumber));
    }

    /**
     * @return inclusive length of an IBAN in this scheme
     */
    public int getLength() {
        return length;
    }

    /**
     * Do the supplied IBAN elements represent a valid IBAN for this scheme?
     *
     * @return true if all elements are valid for this scheme
     * @throws InvalidIBANException if invalid according to this IBAN scheme
     */
    void validate(String bank, String branch, String account) {
        try {
            rules.get(PartCode.BANK).validate(bank);
            rules.get(PartCode.BRANCH).validate(branch);
            rules.get(PartCode.ACC).validate(account);
        } catch (Exception e) {
            throw new InvalidIBANException(this.countryCode, e.getMessage(), bank + branch + account);
        }
    }

    /**
     * Is the supplied IBAN valid for this scheme?
     *
     * @param iban number
     * @return true is <code>iban</code> is a valid IBAN as defined by this scheme
     * @throws InvalidIBANException if invalid according to this IBAN scheme
     */
    void validate(IBAN iban) {
        validate(iban.getBankCode(), iban.getBranchCode(), iban.getAccountNumber());
        if (validate(iban.externalForm()) == false) {
            throw new InvalidIBANException(iban.getCountryCode(), errorMessage, iban.externalForm());
        }
    }

    /**
     * Completely validate an IBAN Currently validation checks that the length is at least 5 chars:
     * (2 country code, 2 verifying digits, and 1 BBAN) checks the country code to be valid and the
     * BBAN valid according to the verifying digits
     *
     * @return <code>true</code> if the IBAN is found to be valid and <code>false</code> in other case
     */
    private boolean validate(String code) {
        final int len = code.length();
        if (len < 4) {
            this.errorMessage = "Too short (expected at least 4, got " + len + ")";
            return false;
        }
        // throws an IAE if the code is not a valid value:
        try {
            ISO3166.valueOf(code.substring(0, 2));
        } catch (IllegalArgumentException e) {
            this.errorMessage = "Bad ISO3166 country code: " + code.substring(0, 2);
            return false;
        }

        try {
            new Integer(code.substring(2, 4)).intValue();
        } catch (NumberFormatException e) {
            this.errorMessage = "Bad verification code: " + code.substring(2, 4);
            return false;
        }

        final String bban = code.substring(4);
        if (bban.length() == 0) {
            this.errorMessage = "Empty BBAN";
            return false;
        }

        if (modulo97(translateChars(bban + code.substring(0, 4))) != 1) {
            this.errorMessage = "Mod97 checksum failed";
            return false;
        }

        return true;
    }

    private static final BigInteger BI_97 = new BigInteger("97");
    private static final BigInteger BI_98 = new BigInteger("98");

    private static int modulo97(String bban) {
        BigInteger b = new BigInteger(bban);
        b = b.divideAndRemainder(BI_97)[1];
        b = BI_98.min(b);
        b = b.divideAndRemainder(BI_97)[1];
        return b.intValue();
    }


    /**
     * Translate letters to numbers, also ignoring non-alphanumeric characters
     *
     * @param bban
     * @return the translated value
     */
    private static String translateChars(final String bban) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < bban.length(); i++) {
            final char c = bban.charAt(i);
            if (Character.isLetter(c)) {
                result.append(Character.getNumericValue(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static IBANScheme lookupScheme(ISO3166 cc) {
        return schemes.get(cc);
    }

    @Override
    public String toString() {
        return countryCode + "=" + rules.toString();
    }

    /**
     * Create a IBANScheme from the specification passed in string form
     * <p/>
     * Example specification:
     * <pre>
     *   "BANK=4c;BRANCH=6n,ACC=8n"
     * </pre>
     * defines an IBAN scheme where the country code is "20", the national
     * dialing code is in the supplied set of values and the subscriber number
     * is seven digits long, and the complete number is eleven digits long
     *
     * @param specification string describing the scheme
     * @param schemeName    identifying the scheme
     * @return a new scheme initialized from the supplied specification
     */
    private static IBANScheme parse(final ISO3166 cc, final String specification) {
        final String[] spec = specification.split(";");
        assert spec.length == 3 : "part rules for bank, branch and account specified";

        Map<PartCode, IBANRule> parts = getPartRules(spec);
        IBANRule bankRule = parts.get(PartCode.BANK);
        bankRule.offset = 4;
        IBANRule branchRule = parts.get(PartCode.BRANCH);
        branchRule.offset = bankRule.offset + bankRule.length;
        IBANRule accRule = parts.get(PartCode.ACC);
        accRule.offset = branchRule.offset + branchRule.length;
        return new IBANScheme(cc, parts, bankRule.offset + bankRule.length + branchRule.length + accRule.length);
    }

    /**
     * For each part, assign the rule from the specification array supplied
     * (this is the set of allowed values for that part)
     *
     * @param valueSet array of allowed values
     * @param rules    for the parts of the IBAN
     */
    private static Map<PartCode, IBANRule>
    getPartRules(final String[] valueSet) {
        final String propString = Joiner.on(";").join(valueSet);
        final Map<String, String> propMap = Splitter.on(';').trimResults().withKeyValueSeparator("=").split(propString);

        final Map<PartCode, IBANRule> result = new HashMap<>(3);
        for (Map.Entry<String, String> spec : propMap.entrySet()) {
            final PartCode key = PartCode.valueOf(spec.getKey().trim().toUpperCase());
            result.put(key, new IBANRule(spec.getValue().trim()));
        }
        return result;
    }

    /**
     * Iterate over the schemes in the supplied properties map, parsing each
     * definition and storing it against the scheme key in the resulting map
     *
     * @param schemes defined as property key/value pairs
     * @return map of scheme keys (hash of country code + length) to definitions
     */
    private static Map<ISO3166, IBANScheme>
    getSchemeMap(final Properties schemes) {
        assert schemes.size() > 0;
        final Map<ISO3166, IBANScheme> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : schemes.entrySet()) {
            ISO3166 cc = ISO3166.valueOf((String) entry.getKey());
            result.put(cc, IBANScheme.parse(cc, (String) entry.getValue()));
        }
        assert result.size() == schemes.size();
        return result;
    }

    /* inner class for parsing and validating against the IBAN scheme definition rules */
    private static final class IBANRule {
        private enum DataType {N, A, C}

        private static final Map<DataType, String> Matchers = new HashMap<DataType, String>(3) {{
            put(DataType.N, "[0-9]");
            put(DataType.A, "[A-Z]");
            put(DataType.C, "[a-zA-Z0-9]");
        }};
        private final int length;
        private int offset;
        private final Pattern matcher;

        /**
         * Parse the specification for this number-part rule and initialize
         * the current rule object
         * IE:BANK=c4;BRANCH=n6,ACC=n8
         *
         * @param specification of type and length (e.g., n4, for a four-digit numeric part)
         */
        IBANRule(String specification) {
            if (specification == null || specification.length() < 2) {
                throw new IllegalArgumentException("Part specification must be type + length, e.g., n4");
            }
            specification = specification.trim();
            DataType type = DataType.valueOf(specification.substring(0, 1).toUpperCase());
            this.length = Integer.parseInt(specification.substring(1));
            matcher = Pattern.compile(Matchers.get(type) + "{" + length + "}");
        }

        /**
         * If the rule contains a restriction of values for this part,
         * is the supplied value in that allowed set?
         *
         * @param value of the rule
         * @return true if values is not empty and it contains the supplied value
         * @throws IllegalStateException is the value does not match this rule
         */
        void validate(String value) {
            if (matcher.matcher(value.trim()).matches() == true) {
                return;
            }
            throw new IllegalStateException("Part " + value + " invalid (does not match \"" + matcher.pattern() + "\")");
        }

        @Override
        public String toString() {
            return matcher + "(" + length + ")";
        }
    }

    /**
     * Load the IBAN scheme definitions from a properties file specified
     * by the system property <code>IBAN_PROPERTIES_KEY</code>
     *
     * @return the loaded schema map
     * @throws RuntimeException
     */
    private static Map<ISO3166, IBANScheme> loadSchemeDefinitions() {
        String ibanFormats = System.getProperty(IBAN_PROPERTIES_KEY, IBAN_PROPERTIES_DEFAULT);
        return getSchemeMap(PropertyLoader.getProperties(ibanFormats));
    }

    /**
     * Clear the currently registered IBAN schemes in this singleton, and reload
     * the scheme definitions from the resource supplied<br/>
     *
     * @param schemeResource location (e.g., filename, URL) of IBAN scheme definitions
     */
    public static void loadScheme(String schemeResource) {
        schemes.clear();
        schemes.putAll(getSchemeMap(PropertyLoader.getProperties(schemeResource)));
    }

    /**
     * Reset the IBAN scheme configuration to its default value
     */
    public static void resetScheme() {
        loadScheme(IBAN_PROPERTIES_DEFAULT);
    }

    /**
     * @param rule  defining the part to be extracted
     * @param value representing an IBAN number
     * @return part of the supplied <code>value</code> defined by <code>rule</code>
     */
    private String getPart(IBANRule rule, String value) {
        return value.substring(rule.offset, rule.offset + rule.length);
    }

    /**
     * @param value representing an IBAN number
     * @return the bank code segment extracted from the supplied <code>value</code>
     */
    String getBankCode(String value) {
        return getPart(rules.get(PartCode.BANK), value);
    }

    /**
     * @param value representing an IBAN number
     * @return the branch code segment extracted from the supplied <code>value</code>
     */
    String getBranchCode(String value) {
        return getPart(rules.get(PartCode.BRANCH), value);
    }

    /**
     * @param value representing an IBAN number
     * @return the account segment extracted from the supplied <code>value</code>
     */
    String getAccountNumber(String value) {
        return getPart(rules.get(PartCode.ACC), value);
    }

}
