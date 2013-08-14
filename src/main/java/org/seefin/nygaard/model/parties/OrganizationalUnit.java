package org.seefin.nygaard.model.parties;

import org.seefin.nygaard.model.identifiers.Identity;

/**
 * Organizational sub unit, such as a branch
 * 
 * @author phillipsr
 *
 */
public class OrganizationalUnit
	extends Organization
{
	private final Organization parent;

	/**
	 * @param id
	 * @param commonName
	 */
	public OrganizationalUnit ( Identity id, Organization parent, String commonName)
	{
		super ( id, commonName);
		this.parent = parent;
	}

	/** @return the parent organization of this unit */
	public Organization getParent () { return parent; }

	@Override
	public String
	toString()
	{
		return super.toString () + " parent=" + parent;
	}
}
