package org.seefin.nygaard.model.identifiers;


/**
 * General concept of a reference code, designed to be extended via inheritance
 * to model the rules and formats of specific reference codes, or as a holder
 * for reference codes for which no rules are known
 * <p/>
 * The contract is to maintain immutability of reference code instances and to
 * ensure that the class invariants hold after construction (and for evermore),
 * so is advisable to make all direct sub-classes themselves final, and to 
 * extend the <code>validate</code> method to enforce any, new and stronger
 * class invariants
 * 
 * @author phillipsr
 *
 */
public class ReferenceCode
	implements Comparable<ReferenceCode>, Identity
{
	private final String value;
	
	/**
	 * Instantiate a Reference code from the supplied value
	 * 
	 * @param value representing the reference code
	 * @throws IllegalArgumentException if the supplied value is not valid (null or empty)
	 */
	public ReferenceCode ( String value)
	{
		this.value = validate ( value);
	}
	
	/**
	 * Validate the supplied value, performing any transforms 
	 * appropriate before returning a valid result
	 * <p/>
	 * This method should be over-written to enforce the class
	 * invariant of specific reference number types
	 * 
	 * @param value
	 * @return a valid and consistent reference code value
	 * @throws IllegalArgumentException if the supplied value is not valid (null or empty)
	 */
	public String
	validate ( String value)
	{
		if ( value == null)
		{
			throw new IllegalArgumentException ( "code value may not be null");
		}
		String result = value.trim();
		if ( result.isEmpty () == true)
		{
			throw new IllegalArgumentException ( "code value may not be empty");
		}
		return result;
	}
	
	@Override
	public String
	toString()
	{
		return value;
	}
	
	@Override
	public boolean
	equals ( Object other)
	{
		if ( this == other)
		{
			return true;
		}
		if ( other == null || other.getClass() != this.getClass())
		{
			return false;
		}
		return value.equals(((ReferenceCode)other).value);
	}

	@Override
	public int
	hashCode()
	{
		return value.hashCode();
	}

	@Override
	public int
	compareTo ( ReferenceCode other)
	{
		return value.compareTo ( other.value);
	}

	/** {@inheritDoc} */
	@Override
	public String
	externalForm ()
	{
		return toString();
	}
	
}
