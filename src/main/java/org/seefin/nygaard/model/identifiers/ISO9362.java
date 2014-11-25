package org.seefin.nygaard.model.identifiers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seefin.nygaard.model.locations.ISO3166;


/**
 * ISO 9362 (Swift/BIC/BEI) Code: Unique identification code for both financial and non-financial institutions
 * <p/>
 * The latest edition is ISO 9362:2009 (dated 2009-10-01). The SWIFT code is 8 or 11 characters, made up of:
 * 4 letters: Institution Code or bank code.
 * 2 letters: ISO 3166-1 alpha-2 country code
 * 2 letters or digits: location code
 * 	if the second character is "0", then it is typically a test BIC as opposed to a BIC used on the live network.
 * 	if the second character is "1", then it denotes a passive participant in the SWIFT network
 * 	if the second character is "2", then it typically indicates a reverse billing BIC, where the recipient pays for the message
 * as opposed to the more usual mode whereby the sender pays for the message.
 * 3 letters or digits: branch code, optional ('XXX' for primary office)
 * <p/>
 * Where an 8-digit code is given, it may be assumed that it refers to the primary office.
 * 
 * @author phillipsr
 *
 */
public class ISO9362
	implements Comparable<ISO9362>, Identity
{
	private static final Matcher InstitutionCodeMatcher = Pattern.compile ( "[A-Z]{4}").matcher("");
	private static final Matcher LocationCodeMatcher = Pattern.compile ( "[A-Z0-9]{2}").matcher("");
	private static final Matcher BranchCodeMatcher = Pattern.compile ( "[A-Z0-9]{3}").matcher("");
	private final String institutionCode;
	private final ISO3166 countryCode;
	private final String locationCode;
	private final String branchCode;
	private final String code; // external-form code, made up of the above elements
	
	public ISO9362 ( String institutionCode, ISO3166 countryCode, String locationCode, String branchCode)
	{
		if ( institutionCode == null)
		{
			throw new IllegalArgumentException ( "InstitutionCode cannot be null");
		}
		institutionCode = institutionCode.trim().toUpperCase ();
		InstitutionCodeMatcher.reset ( institutionCode);
		if ( InstitutionCodeMatcher.matches() == false)
		{
			throw new IllegalArgumentException ( 
					"InstitutionCode (" + institutionCode + ") does not match: " + InstitutionCodeMatcher.pattern ());
		}
		this.institutionCode = institutionCode;
		
		this.countryCode = countryCode;
		
		if ( locationCode == null)
		{
			throw new IllegalArgumentException ( "LocationCode cannot be null");
		}
		locationCode = locationCode.trim().toUpperCase ();
		LocationCodeMatcher.reset ( locationCode);
		if ( LocationCodeMatcher.matches() == false)
		{
			throw new IllegalArgumentException ( 
					"LocationCode (" + locationCode + ") does not match: " + LocationCodeMatcher.pattern ());
		}
		this.locationCode = locationCode;
		
		if ( branchCode != null && branchCode.isEmpty () == false)
		{
			branchCode = branchCode.trim().toUpperCase ();
			BranchCodeMatcher.reset ( branchCode);
			if ( BranchCodeMatcher.matches() == false)
			{
				throw new IllegalArgumentException ( 
						"BranchCode (" + branchCode + ") does not match: " + BranchCodeMatcher.pattern ());
			}
		}
		this.branchCode = branchCode;
		code = institutionCode + countryCode + locationCode + branchCode;
	}
	
	public ISO9362 ( String institutionCode, ISO3166 countryCode, String locationCode)
	{
		this ( institutionCode, countryCode, locationCode, "");
	}
	
	/**
	 * Factory to create a BIC code from the supplied string
	 * @param code
	 * @return
	 */
	public static ISO9362
	valueOf ( String code)
	{
		if ( code == null)
		{
			throw new IllegalArgumentException ( "ISO9362 code cannot be null");
		}
		code = code.trim ();
		if ( code.length () != 8 && code.length () != 11)
		{
			throw new IllegalArgumentException ( "ISO9362 code must be 8 or 11 in length");
		}
		String institutionCode = code.substring ( 0, 4);
		ISO3166 countryCode = ISO3166.valueOf ( code.substring ( 4, 6));
		String locationCode = code.substring ( 6, 8);
		String branchCode = "";
		if ( code.length () == 11)
		{
			branchCode = code.substring ( 8);
		}
		return new ISO9362 ( institutionCode, countryCode, locationCode, branchCode);
	}

	public String getInstitutionCode () { return institutionCode; }
	public ISO3166 getCountryCode () { return countryCode; }
	public String getLocationCode () { return locationCode; }
	public String getBranchCode () { return branchCode; }
	
	@Override
	public String
	toString()
	{
		return "ISO9362:" + code;
	}

	@Override
	public int compareTo ( ISO9362 other)
	{
		return code.compareTo ( other.code);
	}
	
	@Override
	public boolean
	equals(Object other)
	{
		if ( this == other)
		{
			return true;
		}
		if ( other == null || other.getClass() != this.getClass())
		{
			return false;
		}
		return code.equals(((ISO9362)other).code);
	}

	@Override
	public int
	hashCode()
	{
		return code.hashCode();
	}

	@Override
	public String externalForm ()
	{
		return code;
	}
	
}
