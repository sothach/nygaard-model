package org.seefin.nygaard.model.parties;

/**
 * @author phillipsr
 *
 */
public class Supplier
	extends PartyRole
{
	public Supplier ( Party actor)
	{
		if ( actor == null)
		{
			throw new IllegalArgumentException ( "Actor cannot be null");
		}
		this.actor = actor;
	}
}
