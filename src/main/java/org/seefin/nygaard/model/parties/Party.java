package org.seefin.nygaard.model.parties;


import java.io.Serializable;

import org.seefin.nygaard.model.identifiers.Identity;

/**
 * Abstract notion of a physical or organizational entity that may be 
 * involved in a transaction or similar activity<p/>
 * This is intended as a marker interface for type correctness purposes,
 * but may gain state and/or behavior during refactoring
 * 
 * @author phillipsr
 *
 */
public interface Party
	extends Serializable
{
	/** @return Name by which the party is commonly referenced, might not be unique or complete */
	public String getCommonName();
	
	// perhaps the concept of identity is a common property of all parties?
	public Identity getId ();
}
