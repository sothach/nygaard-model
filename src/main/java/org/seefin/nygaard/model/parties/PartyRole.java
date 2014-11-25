package org.seefin.nygaard.model.parties;

import java.io.Serializable;

import org.seefin.nygaard.model.identifiers.Identity;


/**
 * Generic concept of a role, whereby an individual or organization (the <i>actor</i>)
 * carries out certain tasks and takes on responsibilities within a limited set of
 * circumstances
 * 
 * @author phillipsr
 *
 */
public class PartyRole
	implements Serializable
{
	/** the person or organization performing the role */
	protected Party actor;
	/** the common label by which the role is known */
	protected String label;

	/** @return the party performing this role */
	public Party getActor() { return actor; }
	
	/** @return the identity of the actor party */
	public Identity getIdentity() { return actor.getId(); }
	
	/** @return the label for this role */
	public String getLabel() { return label; }

}
