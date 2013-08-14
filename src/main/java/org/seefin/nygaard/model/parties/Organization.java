package org.seefin.nygaard.model.parties;

import org.seefin.nygaard.model.identifiers.Identity;

/**
 * @author phillipsr
 *
 */
public class Organization
	implements Party
{
	protected Identity identity;
	protected String commonName;
	
	public Organization ( Identity id, String commonName)
	{
		identity = id;
		this.commonName = commonName;
	}
	
	@Override
	public String getCommonName ()
	{
		return commonName;
	}

	@Override
	public Identity getId ()
	{
		return identity;
	}
	
	@Override
	public String
	toString()
	{
		return commonName + "(" + identity + ")";
	}

}
