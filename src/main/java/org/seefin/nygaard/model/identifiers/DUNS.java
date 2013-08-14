package org.seefin.nygaard.model.identifiers;



/**
 * The Data Universal Numbering System, abbreviated as DUNS or D-U-N-S, is a system developed 
 * and regulated by Dun & Bradstreet (D&B), that assigns a unique numeric identifier, referred 
 * to as a "DUNS number" to a single business entity.
 * <p/>
 * The DUNS number is a nine-digit number, issued by D&B, assigned to each business location in the 
 * D&B database, having a unique, separate, and distinct operation for the purpose of identifying them.<br/> 
 * The DUNS number is random, and the digits have no apparent significance. Until approximately December 2006, 
 * the DUNS number contained a mod-10 check digit to support error detection. Discontinuing the check digit
 * increased the inventory of available DUNS numbers by 800 million
 * <p/>
 * Example DUNS number is 039189524, MobiPay: 031090010, IBM DUNS Number: 835130485
 * 
 * @author roy.phillips
 *
 */
public final class DUNS
	implements Identity, Comparable<DUNS>
{
	private final int value;
    
	/**
	 * Instantiate a new DUNS number having the value set from the supplied number, 
	 * 
	 * @param number integer representation of a DUNS
	 */
	public DUNS ( int number)
	{
		value = number;
	}

	/**
	 * Create a DUNS from the string representation supplied,
	 * 
	 * @param dunsString
	 * @return new DUNS instance initialized from parameter
	 */
	public static DUNS
	parse ( String dunsString)
	{
		return new DUNS ( Integer.parseInt ( dunsString));
	}
	
	/**
	 * Create a new DUNS having the value set from the supplied
	 * number
	 * 
	 * @param number
	 * @return
	 */
	public static DUNS
	valueOf(int number)
	{
		return new DUNS ( number);
	}
	
	/**
	 * Answer with the canonical DUNS format String for the current number
	 */
	@Override
	public String
	externalForm ()
	{
		return String.format ( "%09d", value);
	}
	
	/** Answer with the simple numeric format String */
	@Override
	public String
	toString()
	{
		return "DUNS:"+Integer.toString ( value);
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
		return Integer.valueOf(value).equals(((DUNS)other).value);
	}

	@Override
	public int
	hashCode()
	{
		return Integer.valueOf(value).hashCode();
	}
	
	/**
	 * Answer with a integer representation of this DUNS's number
	 * 
	 * @return
	 */
	public long
	intValue()
	{
		return value;
	}

	@Override
	public int
	compareTo ( DUNS other)
	{
		return Integer.valueOf(value).compareTo(other.value);
	}

}
