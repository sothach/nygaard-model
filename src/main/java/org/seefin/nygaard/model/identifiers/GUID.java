package org.seefin.nygaard.model.identifiers;

import java.net.URI;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A globally-unique identity, suitable for use as a primary key, transactions id, etc.,
 * that can be stored in a CHAR(32) field, for example
 * <p/>
 * Note: this implementation relies upon the Java UUID class to generate the ID value, 
 * which uses type 4 generation (fully random); it may be better to change this to 
 * use type 1 (MAC address based) generation: this would require an external implementation
 * @author phillipsr
 *
 */
public final class GUID
	implements Identity, Comparable<GUID>
{
	private static final Matcher UUIDMatcher = Pattern.compile ( "[\\p{XDigit}]{32}").matcher("");
	private static final Matcher CanonicalUUIDMatcher 
		= Pattern.compile ( "([\\p{XDigit}]{8})-([\\p{XDigit}]{4})-([\\p{XDigit}]{4})-([\\p{XDigit}]{4})-([\\p{XDigit}]{12})").matcher("");
	private final String identity;
	
	private GUID()
	{
		this ( UUID.randomUUID ().toString ());
	}
	
	private GUID ( String externalForm)
	{
		// convert to a 32 hex digit string
		identity = externalForm.replaceAll ( "[^A-Za-z0-9]", "");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String
	externalForm ()
	{
		return identity;
	}
	
	/**
	 * {@inheritDoc}
	 * @return the actual String value of this identity, as there is no requirement 
	 * 				to return an obfusticated identity value
	 */
	@Override
	public String
	toString ()
	{
		return 
				identity.substring ( 0,4) + "/../" + identity.substring ( identity.length ()-4);
	}
	
	public static GUID
	parse ( String externalForm)
	{
		return new GUID ( externalForm);
	}
	
	/**
	 * @return a new UniqueIdentity instance
	 */
	public static GUID
	createUniqueId()
	{
		return new GUID ();
	}
	
	public static boolean
	isValid ( String anIdString)
	{
		return anIdString != null && anIdString.isEmpty () == false 
				&& ( UUIDMatcher.reset ( anIdString).matches() 
						|| CanonicalUUIDMatcher.reset ( anIdString).matches() );
	}
	
	public URI
	asURN()
	{
		// urn:uuid:3F2504E0-4F89-11D3-9A0C-0305E82C3301
		return URI.create ( "urn:uuid:" + identity.toUpperCase ());
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
		return identity.equals ( ((GUID)other).identity);
	}

	@Override
	public int
	hashCode()
	{
		return identity.hashCode();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int
	compareTo ( GUID otherId)
	{
		return identity.compareTo ( otherId.identity);
	}

}
