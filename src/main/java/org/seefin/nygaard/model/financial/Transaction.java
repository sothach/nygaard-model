package org.seefin.nygaard.model.financial;

import java.io.Serializable;

import org.seefin.nygaard.model.identifiers.Identity;


/**
 * Just a marker interface for now, to allow transactions to be passed around by routers,
 * without having to know their true purpose
 * <p/>
 * Some common patterns may emerge that would enable generic messages to be defined here,
 * watch this space
 *  
 * @author phillipsr
 *
 */
public interface Transaction
	extends Serializable
{
	/**
	 * @return the unique transaction identifier
	 */
	public Identity getIdentity();
}
