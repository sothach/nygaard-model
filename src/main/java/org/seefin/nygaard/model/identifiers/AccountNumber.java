package org.seefin.nygaard.model.identifiers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author phillipsr
 *
 */
public abstract class AccountNumber
	implements  Comparable<AccountNumber>, Identity
{
	private static final String STARS = "******************************";
	private static final String PUNCTUATION = " \\.,_/:;-"; 
	private static final Matcher PUNCTUATIONCleaner 
		= Pattern.compile ( "[" + PUNCTUATION + "]").matcher("");
	
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
		return toString().equals(((AccountNumber)other).toString());
	}

	@Override
	public int
	hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public int
	compareTo ( AccountNumber other)
	{
		return toString().compareTo(other.toString());
	}
	
	/**
	 * @param code
	 * @return a copy of the supplied <code>code</code> with insignificant
	 * characters removed:<br/>
	 * Spaces, slash, period, comma, underscore, colon, semicolon
	 */
	protected static String
	sanitize ( String code)
	{
		if ( code == null)
		{
			return null;
		}
		PUNCTUATIONCleaner.reset ( code);
		return PUNCTUATIONCleaner.replaceAll ( "");
	}
	
	/**
	 * @param value of the account number
	 * @param initial length to be displayed in clear
	 * @param end length to be displayed in clear
	 * @return a version of the supplied account number, with all but
	 * 			the specified initial and final portion replaced by stars
	 */
	protected static String
	obsusticate ( final String value, int initial, int end)
	{
		int length = value.length ();
		initial = initial < length ? initial : 1;
		end = end < length ? end : 1;
		int oblen = length-initial-end;
		oblen = oblen < 3 ? 3 : oblen; 
		return value.substring ( 0, initial) + STARS.substring ( 0, oblen)
				+ value.substring ( length-end);
	}

}
