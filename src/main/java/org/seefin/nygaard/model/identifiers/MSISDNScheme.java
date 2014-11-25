package org.seefin.nygaard.model.identifiers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * Creates MSISDN numbers based on schemes, loaded from a resource named by 
 * the system property <code>msisdn.scheme.specification</code> (e.g.,
 * <code>-Dmsisdn.scheme.specification=path/to/msisdn.file</code>)
 * <p/>
 * The file format consists of a unique key (usually the ISO country code, and
 * optionally, MNO name and/or a discriminator), followed by a specification of the
 * the number scheme, for example:
 * <pre>
 *   EG.9=2,2,7;CC=20;NDC=10,11,12,14,16,17,18,19
 * </pre>
 * Describes a MSISDN scheme where the country code part is "20", the NDC part is
 * two digits long and in the specified set, and the subscriber number is seven
 * digits long
 * <p/>
 * This is the 'companion class' (public) that Josh Bloch recommends when working with
 * immutable value objects, to perform factory type operations, such as value construction
 * @author roy.phiilips
 *
 */
public class MSISDNScheme
{
    private enum PartCode
	{
		CC, NDC, SN
	}

    // ... loads MSISDN specifications from file named by this property
    public static final String MSISDN_PROPERTIES_KEY = "msisdn.scheme.specification";
    private static final String MSISDN_PROPERTIES_DEFAULT = "org/seefin/nygaard/model/identifiers/MSISDNScheme.properties";
    // get known MSISDN schemes from property file
	private static final Map<Integer,MSISDNScheme> schemes = loadSchemeDefinitions();
	// exception message prefixes (public for testability)
	public static final String NO_SCHEMAS_LOADED = "No MSISDN schemes were loaded";
    public static final String SCHEMA_LOCATION_INVALID = "Schema location may not be null or empty";
	public static final String INVALID_SCHEMA_PART_LENGTH = "Invalid part length ( <= 0 ) at index ";
    public static final String UNRECOGNIZED_SCHEME = "MSISDN Unrecognized scheme";
	
	// per-scheme instance values:
	private final Map<PartCode,MSISDNRule> rules;
	private final String name;
	private final int length;
	final long ccfactor; // factor to decode CC from a long value
	final long ndcfactor; // factor to decode NDC from a long value
	
	/**
	 * Constructor called from the static scheme loader to instantiate a scheme
	 * from the parsed specification, as represented by the parameters below
	 * 
	 * @param schemeName unique name of the scheme (key)
	 * @param rules set of rules defining the parts
	 * @param length total length of a number in this scheme
	 */
	private MSISDNScheme ( String schemeName, Map<PartCode, MSISDNRule> rules, int length)
	{
		this.name = schemeName;
		this.length = length;
		this.rules = rules;
		int snlength = rules.get(PartCode.SN).length;
		int ndcLength = rules.get(PartCode.NDC).length;
		this.ccfactor =  (long) Math.pow ( 10, ndcLength + snlength);
		this.ndcfactor = (long) Math.pow ( 10, snlength);
	}

	/**
	 * Is the supplied MSISDN valid for this scheme?
	 *
	 * @param msisdn number
	 * @return true is <code>msisdn</code> is a valid MSISDN as defined by this scheme
	 */
	public boolean
	isValid ( MSISDN msisdn)
	{
		return isValid( msisdn.getCC(), msisdn.getNDC(), msisdn.getSN());
	}
	
	/**
	 * Do the supplied MSISDN elements represent a valid MSISDN for this scheme?
	 *
	 * @return true if all elements are valid for this scheme
	 */
	boolean
	isValid ( int cc, int ndc, int sn)
	{
		return rules.get(PartCode.CC).isValid(cc)
			&& rules.get(PartCode.NDC).isValid(ndc)
			&& rules.get(PartCode.SN).isValid(sn);
	}
	
	/**
	 * MSISDN factory method to match the input string against the known
	 * set of MSISDN schemes and instantiate an MSISDN object
	 * 
	 * @param msisdnString from which to initialize the result
	 * @return MSISDN create from the input string
	 * @throws IllegalArgumentException if the parameter is not a valid and known MSISDN
	 */
	static MSISDN
	createMSISDN ( String msisdnString)
	{
		if ( msisdnString == null || msisdnString.trim().length() == 0)
		{
			throw new IllegalArgumentException (
					"MSISDN candidate string must be non-null and non-empty: " + msisdnString);
		}
		String candidate = normalize ( msisdnString);
		MSISDN result = null;
		for ( int ccSize = 3; ccSize > 0 && result == null; ccSize--)
		{
			result = lookupByCC ( ccSize, candidate);
		}
		if ( result == null)
		{
			throw new IllegalArgumentException (
			        UNRECOGNIZED_SCHEME + " for: " + msisdnString
			        + " (known schemes=" + schemes.values () + ")");
		}
		return result;
	}
	
	public static MSISDNScheme
	getSchemeForCC ( int cc, int length)
	{
		return schemes.get( createKey(cc,length));
	}
	
	/**
	 * Look-up the scheme for the country code represented by the first <code>ccSize</code>
	 * digits of the supplied string, continuing to match the NDC and SN if found, and 
	 * creating the appropriate MSISDN
	 * 
	 * @param ccSize assumed size of country code prefix
	 * @param candidate the normalized MSISDN string
	 * @return a MSISDN version of the candidate string, if valid, else null
	 */
	private static MSISDN
	lookupByCC ( int ccSize, String candidate)
	{
		if ( candidate.length () < ccSize)
		{
			return null;
		}
		int tryCC = Integer.valueOf(candidate.substring(0,ccSize));
		MSISDNScheme scheme = schemes.get( createKey(tryCC,candidate.length()));
		if ( scheme == null)
		{
			return null;
		}
		MSISDNRule ccRule = scheme.rules.get(PartCode.CC);
		if ( ccRule.isValid( tryCC) == false)
		{
			return null;
		}
		MSISDNRule ndcRule = scheme.rules.get(PartCode.NDC);
		int tryNDC = Integer.valueOf(candidate.substring(ccSize,ccSize+ndcRule.length));
		if ( ndcRule.isValid(tryNDC) == false)
		{
			return null;
		}
		String snString = candidate.substring(ccSize+ndcRule.length);
		if ( snString.length() != scheme.rules.get(PartCode.SN).length)
		{
			return null;
		}
		int sn = Integer.valueOf(snString );
		return MSISDN.create ( tryCC, tryNDC, sn, scheme);
	}

	/**
	 * process a external MSISDN string into a normalized string, removing
	 * non-digits, and country code indicators ('+', '00') if present
	 * @param msisdnString possibly non-normalized
	 * @return normalized copy of the input string
	 */
	private static String
	normalize ( String msisdnString)
	{
		String result = msisdnString.trim().replaceAll( "[^\\d]", "");
		result = result.replaceFirst("^+", "");
		result = result.replaceFirst("^00", "");
		return result;
	}

	@Override
	public String
	toString()
	{
		return name + "=" + rules.toString();
	}
	
	/**
	 * Create a MSISDNScheme object representing the scheme specification
	 * passed in string form
	 * <p/>
	 * Example specification:
	 * <pre>
	 *   "2,2,7;CC=20;NDC=10,11,12,14,16,17,18,19"
	 * </pre>
	 * defines an MSISDN scheme where the country code is "20", the national
	 * dialing code is in the supplied set of values and the subscriber number
	 * is seven digits long, and the complete number is eleven digits long
	 * @param specification string describing the scheme
	 * @param schemeName identifying the scheme
	 * @return a new scheme initialized from the supplied specification
	 */
	static MSISDNScheme
	parse ( final String specification, String schemeName)
	{
		final String[] spec = specification.split(";");
		assert spec.length > 0 : "At minimum, parts lengths specified";
		Map<PartCode,MSISDNRule> rules = new HashMap<>();
		
		int schemeLength = getPartLengths ( rules, spec[0]);
		assert schemeLength <= 15 : "MSISDN in fifteen-digit numbering space";
		setPartRules ( rules, Arrays.copyOfRange ( spec, 1, spec.length));
		
		return new MSISDNScheme ( schemeName, rules, schemeLength);
	}

	/**
	 * For each part, set the length of that part, as defined by the supplied
	 * specification string, in the supplied <code>rules</code> map
	 * 
	 * @param lengthSpec comma-separated part length specification (e.g., "2,2,7")
	 * @param rules map of rule objects, one for each part: CC, NDC and SN
	 * @return the total length of the MSISDN of the specified scheme
	 */
	private static int
	getPartLengths (  Map<PartCode, MSISDNRule> rules, final String lengthSpec)
	{
		final String[] partsLengths = lengthSpec.split(",");
		assert partsLengths.length == 3 : "Lengths of all three parts provided";
		final PartCode[] keys = { PartCode.CC, PartCode.NDC, PartCode.SN};
		int schemeLength = 0;
		for ( int i = 0; i < keys.length; i++)
		{
			int length = Integer.parseInt(partsLengths[i].trim());
            if(length <= 0){
                throw new IllegalArgumentException( INVALID_SCHEMA_PART_LENGTH + i + " in " + lengthSpec);
            }
			rules.put ( keys[i],  new MSISDNRule(length));
			schemeLength += length;
		}
		return schemeLength;
	}
	
	/**
	 * For each part, assign the rule from the specification array supplied
	 * (this is the set of allowed values for that part)
	 * @param valueSet array of allowed values
	 * @param rules for the parts of the MSISDN
	 */
	private static void
	setPartRules ( Map<PartCode, MSISDNRule> rules, final String[] valueSet)
	{
		for ( String partSpec : valueSet)
		{
			final String[] rule = partSpec.split("=");
			assert rule.length == 2 : "part is " + partSpec;
			final PartCode key = PartCode.valueOf(rule[0].trim());
			rules.get(key).setValues(rule[1].trim());
		}
	}	

	/**
	 * @param cc country code
	 * @param ndc national dialing code
	 * @param sn subscriber number
	 * @return a MSISDN number belonging to this scheme constructed from
	 * 	the parameters supplied
	 */
	long
	longValue(int cc, int ndc, int sn)
	{
		return (cc * ccfactor) + (ndc * ndcfactor) + sn;
	}
	
	/**
	 * @param value a numeric representation of an MSISDN 
	 * @return a MSISDN created from the supplied number
	 */
	public static MSISDN
	fromLong ( long value)
	{
		return createMSISDN(value+"");
	}

	/**
	 * Iterate over the schemes in the supplied properties map, parsing each
	 * definition and storing it against the scheme key in the resulting
	 * map
	 * 
	 * @param schemes defined as property key/value pairs
	 * @return map of scheme keys (hash of country code + length) to definitions
	 */
	private static Map<Integer, MSISDNScheme>
	getSchemeMap ( Properties schemes)
	{
	    assert schemes.size() > 0;
		Map<Integer, MSISDNScheme> result = new HashMap<>();
        for ( Entry<Object, Object> entry : schemes.entrySet())
        {
        	MSISDNScheme scheme = MSISDNScheme.parse((String) entry.getValue(), (String)entry.getKey());
        	Integer cc = (Integer) scheme.rules.get(PartCode.CC).values.toArray()[0];
        	result.put ( createKey(cc,scheme.length), scheme);
        }
        assert result.size() == schemes.size();
        return result;
	}
    
    /**
     * Key to the scheme map is CC left-shifted by four bits plus the MSISDN length-1, this
     * allows up to 15 digits lengths to be specified with an arbitrarily long country code
     * E.g., 353 + 11 => 0x161b, or 1 + 14 (max US number):  0x001e
     */
	private static Integer
	createKey ( int countryCode, int length)
	{
		return (countryCode << 4) + Math.abs(length-1);
	}
    
    private static final class MSISDNRule
    {
    	private final int length;
    	private final Set<Integer> values = new HashSet<>();

    	MSISDNRule ( int length)
    	{
    		this.length = length;
    	}

    	/**
    	 * Parse the specification for this number-part rule and initialize
    	 * the current rule object
    	 * 
    	 * @param specification comma-separated list of valid values (integers)
    	 */
    	void
    	setValues ( String specification)
    	{
    		String[] spec = specification.split(",");		
    		for ( String part : spec)
    		{
    			values.add ( Integer.parseInt(part.trim()));
    		}
    	}
    	
    	/**
    	 * If the rule contains a restriction of values for this part, 
    	 * is the supplied value in that allowed set?
    	 * @param value of the rule
    	 * @return true if values is not empty and it contains the supplied value
    	 */
    	boolean
    	isValid ( Integer value)
    	{
    		return values.isEmpty() || value == 0 ? true : values.contains(value);
    	}
    	
    	@Override
    	public String
    	toString()
    	{
    		return values.toString ();
    	}
    }
    
	/**
	 * Load the MSISDN scheme definitions from a properties file specified
     * by the system property <code>MSISDN_PROPERTIES_KEY</code>
	 * 
	 * @return the loaded schema map
	 * @throws RuntimeException 
	 */
    private static Map<Integer, MSISDNScheme>
    loadSchemeDefinitions()
    {
		String msisdnFormats = System.getProperty ( MSISDN_PROPERTIES_KEY, MSISDN_PROPERTIES_DEFAULT);
		Properties properties = PropertyLoader.getProperties ( msisdnFormats);
        return getSchemeMap ( properties);
    }

    /**
     * Clear the currently registered MSISDN schemes in this singleton, and reload
     * the scheme definitions from the resource supplied<br/>
     * 
     * @param schemeResource location (e.g., filename, URL) of MSISDN scheme definitions
     */
    public static void
    loadScheme ( String schemeResource)
    {
    	schemes.clear ();
		Properties properties = PropertyLoader.getProperties ( schemeResource);
        schemes.putAll ( getSchemeMap ( properties));
    }
    
    /**
     * Clear the currently registered MSISDN schemes in this singleton, and reload
     * the scheme definitions from the resource supplied<br/>
     * 
     * @param schemeResource location (e.g., filename, URL) of MSISDN scheme definitions
     * @throws IOException 
     */
    public static void
    loadSchemeFromFile ( File schemeResource)
    	throws IOException
    {
    	schemes.clear ();
		Properties properties = PropertyLoader.getPropertiesFromFile ( schemeResource);
        schemes.putAll ( getSchemeMap ( properties));
    }

    /**
     * Reset the MSISDN scheme configuration to its default value
     */
    public static void
    resetScheme()
    {
        loadScheme ( MSISDN_PROPERTIES_DEFAULT);
    }

}
