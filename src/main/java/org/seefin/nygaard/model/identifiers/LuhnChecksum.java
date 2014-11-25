package org.seefin.nygaard.model.identifiers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * helper to calculate and verify Luhn checksums
 *<p/>
 * This algorithm was invented by Hans Peter Luhn (July 1, 1896, Barmen, Germany  August 19, 1964)<br/>
 * He joined IBM as a senior research engineer in 1941, and soon became manager of the information retrieval
 * research division.
 * Luhn was the first, or among the first, to work out many of the basic techniques now commonplace
 * in information science
 * 
 * @author phillipsr
 *
 */
public class LuhnChecksum
{	
	private static final Matcher NON_NUMERICS = Pattern.compile ( "[^0-9]").matcher("");
	/**
	 * Calculate the Luhn (Mod 10) checksum for the supplied number string
	 * <ol>
	 * <li>From the rightmost digit, which is the check digit, moving left, double the value of every second digit; 
	 * 	if product of this doubling operation is greater than 10 (e.g., 7 * 2 = 14) then 9 should be subtracted 
	 * 	from the product (e.g. 7 * 2 - 9 = 5)</li>
	 * <li>Sum the digits of the products (e.g., 10: 1 + 0 = 1, 14: 1 + 4 = 5) together with the undoubled digits 
	 * 	from the original number</li>
	 * <li>If the total modulo 10 is equal to 0 (if the total ends in zero) then the number is valid according to 
	 * 	the Luhn formula; else it is not valid</li>
	 * </ol>
	 * @param cardNumber
	 * @return
	 */
	private static int
	calculate ( String number)
	{
		int result = 0;
		boolean toggle = false;
		for (int i = number.length()-1; i >= 0; i--)
		{
			int digit = Character.getNumericValue(number.charAt(i));
			result += toggle ? calcDigit ( digit) : digit;
			toggle = !toggle;
		}
		return result;
	}


	/**
	 * @param digit a 'second digit' from the number string being checked
	 * @return the doubled value of the supplied digit; if this is > 10, subtract 9 from result
	 */
	private static int
	calcDigit ( int digit)
	{
		int addend = digit * 2;
		if (addend > 9)
		{
			addend -= 9; 
		}
		return addend;
	}
	
	public static int
	getCheckDigit ( String candidate)
	{
		if ( candidate == null || candidate.isEmpty ())
		{
			throw new IllegalArgumentException ( "Number string for checksum calculation cannot be null or blank");
		}
		candidate = candidate.trim ();
		if ( NON_NUMERICS.reset ( candidate).matches () == true)
		{
			throw new IllegalArgumentException ( "Number string must be numeric (got: " + candidate + ")");
		}
		return (calculate ( candidate+"0") * 9) % 10;
	}
	/**
	 * Answer true if the check digit (last digit) of the supplied number string
	 * matches the check digit calculated for the rest of the string
	 * 
	 * @param candidate string to validate
	 * @return true if the supplied number string is valid by the Luhn algorithm 
	 */
	public static boolean
	isValid ( String candidate)
	{
		if ( candidate == null || candidate.length () < 2)
		{
			return false;
		}
		candidate = candidate.trim ();
		if ( NON_NUMERICS.reset ( candidate).matches () == true)
		{
			return false;
		}
		return calculate ( candidate) % 10 == 0;
	}
}
