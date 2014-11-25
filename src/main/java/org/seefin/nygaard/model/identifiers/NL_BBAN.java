package org.seefin.nygaard.model.identifiers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dutch BBAN format
 * 
 * @author phillipsr
 *
 */
public final class NL_BBAN
	extends BBAN
{
	private static final Matcher NonNumericMatcher 
		= Pattern.compile ( "[^0-9]").matcher("");
	private static final int[] AC_NO_WEIGHT =
			{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private static final int BANK_ACCOUNT_MAX = 10;
	private static final int BANK_ACCOUNT_MIN = 8;
	private static final String NUMBER_TEMPLATE = "%0" + BANK_ACCOUNT_MAX + "d";
	
	private final String value;
	private transient String errorMessage;
	
	private NL_BBAN()
	{
		value = null;
	}
	
	/**
	 * Create a NL BBAN Account Number from the supplied string
	 * @param code representing the BBAN - must not contain punctuation
	 * @throws IllegalArgumentException if the code does not
	 * 			represent a valid NL BBAN
	 */
	private NL_BBAN ( String code)
	{
		if ( code == null || code.isEmpty () == true)
		{
			throw new IllegalArgumentException ( "Code must not be null or empty");
		}
		if ( validate ( code) == false)
		{
			throw new IllegalArgumentException ( 
					"Code [" + code + "] not valid: " + errorMessage);
		}
		this.value = code;
	}
	
	/**
	 * Create a NL BBAN Account Number from the supplied string
	 * 
	 * @param code representing the BBAN, may contain punctuation
	 * @throws IllegalArgumentException if the code cannot be
	 * 			interpreted as a valid NL BBAN
	 */
	public static NL_BBAN
	create ( String code)
	{
		return new NL_BBAN ( sanitize ( code));
	}
	
	@Override
	public String
	toString()
	{
		return obsusticate ( value, 2,  2);
	}
	
	/**
	 * Method to check an incoming account number for mod 11 compliance
	 * @param accountNb
	 * @return false if not a valid BBAN, sets errorMessage by side-effect
	 * 		to record cause of any validation failure
	 */
	public boolean
	validate ( String accountNb)
	{
		NonNumericMatcher.reset ( accountNb);
		if ( NonNumericMatcher.find() == true)
		{
			errorMessage = "Account number must be numeric";
			return false;
		}
		
		String canonAccNb = null;
		try
		{
			canonAccNb = String.format (
				NUMBER_TEMPLATE, Integer.parseInt ( accountNb));
		}
		catch ( NumberFormatException ex)
		{
			errorMessage = "Account number format: " + ex.getMessage ();
			return false;
		}
		//canonAccNb = formatAccount ( accountNb);

		if ( isBankAccount ( canonAccNb) == false)
		{
			errorMessage = "Not a valid bank account number";
			return true;
		}

		// we don't have to check ac number if less than nine digits long
		if ( canonAccNb.length () != AC_NO_WEIGHT.length)
		{
			return false;
		}

		if ( getMod11Checksum ( canonAccNb) % 11 != 0)
		{
			errorMessage = "Checksum failed";
			return false;
		}

		return true;
	}
	
	/**
	 * public validator
	 * @param code
	 * @return
	 */
	public static boolean
	isValid ( String code)
	{
		NL_BBAN validator = new NL_BBAN();
		return validator.validate ( sanitize ( code));
	}

	private static int
	getMod11Checksum ( String accountNb)
	{
		int result = 0;
		int j = accountNb.length () - 1;
		for ( int i = 0; i < accountNb.length (); i++, j--)
		{
			String iStr = new String ( accountNb.substring ( i, i + 1));
			result += ( new Integer ( iStr) ).intValue () * AC_NO_WEIGHT[j];
		}
		return result;
	}
	
	
	private static boolean 
	isBankAccount ( String accountNb)
	{
		if ( accountNb.startsWith ( "0"))
		{
			// This has already been padded ... so check the padding ...
			if ( accountNb.startsWith ( "000"))
			{
				// This is not a Bank Account
				return false;
			}
			return true;
		}
		// Check if this is a Bank Account or a Post bank Ac
		return accountNb.length () >= BANK_ACCOUNT_MIN 
				&& accountNb.length () <= BANK_ACCOUNT_MAX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String externalForm ()
	{
		return value;
	}
}
