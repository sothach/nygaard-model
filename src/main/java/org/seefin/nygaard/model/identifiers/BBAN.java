package org.seefin.nygaard.model.identifiers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Basic Bank Account Number (BBAN)<p/> 
 * The BBAN format is decided by each national banking community under the restriction that it must be of 
 * a fixed length of case-insensitive alphanumeric characters. It will often include the domestic bank account number, 
 * branch identifier, and potential routing information.
 * 
 * @author phillipsr
 *
 */
public class BBAN
	extends AccountNumber
{
	private static final Matcher NonAlphanumericMatcher 
		= Pattern.compile ( "[^A-Za-z0-9]").matcher("");

	private final String id;
	
	/**
	 * @param code
	 */
	public BBAN ( String code)
	{
		if ( code == null || code.isEmpty ())
		{
			throw new IllegalArgumentException ( "BBAN code cannot be null/blank");
		}
		code = sanitize ( code);
		NonAlphanumericMatcher.reset ( code);
		if ( NonAlphanumericMatcher.find() == true)
		{
			throw new IllegalArgumentException ( "BBAN must be alphanumeric, was: [" + code + "]");
		}
		this.id = code;
	}

	protected BBAN()
	{
		id = null;
	}
	
	@Override
	public String
	toString()
	{
		return obsusticate ( id, 2, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String externalForm ()
	{
		return id;
	}
}
