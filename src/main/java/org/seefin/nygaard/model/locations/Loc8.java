package org.seefin.nygaard.model.locations;



/**
 * Proposed all-Ireland post code
 * E.g., "NW6-00-YD7"
 * <p/>
 * Loc8 Codes are easy to remember and work with Latitude & Longitude or Grid. Their structure is very easy too understand. 
 * For Example, our office Loc8 Code is W8L-82-4YK.
 * <ul>
 * <li>The "W" defines a 90km square which covers most of Cork. The First Character of a Loc8 Code will always be a letter.</li>
 * <li>The Second two characters can be either letters or numbers and define an approximate 3.5 km square zone within the area of "W".</li>
 * <li>The following two numbers define an even smaller area - metres in fact. The next two Characters, "4Y", combined with the
 * first 3, define a 120 meter square locality.</li>
 * <li>The last character "K" is the checker code which is used to check all the other characters in the code. You can find a ZONE 
 * by using just the First 3 characters or a LOCALITY by dropping the numbers between the dashes.</li>
 * </ul>
 * Furthermore, the letters H, I, O and U never appear in the code. The letter C is never used as the first letter of the code; 
 * whereas, the letter G only ever appears as a first character. The letters A and E only appear when their presence would not 
 * lead to a word being spelled. The middle two characters are always numeric. The letters 'BT' are designed to never occur at 
 * the beginning of a Loc8 code, avoiding confusion with Royal Mail postcodes for Northern Ireland, which all begin with those 
 * two letters.
 * @author phillipsr
 *
 */
public class Loc8
	implements PostalCode
{
	@Override
	public String externalForm ()
	{
		return null;
	}

}
