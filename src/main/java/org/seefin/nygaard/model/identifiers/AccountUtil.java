/**
 * 
 */
package org.seefin.nygaard.model.identifiers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author phillipsr
 *
 */
public class AccountUtil
{
	private static final Logger log = LoggerFactory.getLogger(AccountUtil.class);
	
	/**
	 * Answer true if the account number is a valid IBAN or NL BBAN,
	 * after stripping any spaces from the supplied number
	 * 
	 * @param accountNumber
	 * @return
	 */
	public static boolean
	isAccountNumberValid ( String accountNumber)
	{
		String canon = accountNumber != null ? accountNumber.trim () : "";
		canon = canon.replaceAll ( " ", "");
		try
		{
			new BBAN ( canon);
			log.debug ( "validating account number [{}]: valid", accountNumber);
			return true;
		}
		catch ( Exception e)
		{
			log.debug ( "validating account number [{}] : not valid ({})", accountNumber, e.getMessage ());
			return false;	
		}
	}
	
	/**
	 * Validate if the supplied <code>accountNumner</code> is a valid NL BBAN,
	 * returning its canonical form
	 * 
	 * @param accountNumber as String to be validated
	 * @return the canonical form (no spaces) of the account number
	 * 			(if valid), else empty String ("")
	 */
	public static String
	validateBBAN ( String accountNumber)
	{
		String canon = accountNumber != null ? accountNumber.trim () : "";
		canon = canon.replaceAll ( " ", "");
		try
		{
			AccountNumber bban = NL_BBAN.create ( canon);
			log.debug ( "validating NL-BBAN [{}]: valid", accountNumber);
			return bban.toString ();
		}
		catch ( Exception e)
		{
			log.debug ( "validating NL-BBAN [{}] : not valid ({})", accountNumber, e.getMessage ());
			return "";	
		}
	}
	
	/**
	 * Validate if the supplied <code>accountNumner</code> is a valid IBAN,
	 * returning its canonical form
	 * 
	 * @param accountNumber as String to be validated
	 * @return the canonical form (no spaces) of the account number
	 * 			(if valid), else empty String ("")
	 */
	public static String
	validateIBAN ( String accountNumber)
	{
		String canon = accountNumber != null ? accountNumber.trim () : "";
		canon = canon.replaceAll ( " ", "");
		try
		{
			AccountNumber bban = IBAN.parse ( canon);
			log.debug ( "validating IBAN [{}]: valid", accountNumber);
			return bban.toString ();
		}
		catch ( Exception e)
		{
			log.debug ( "validating IBAN [{}] : not valid ({})", accountNumber, e.getMessage ());
			return "";	
		}
	}


	
}
