package org.seefin.nygaard.model.services;

import org.seefin.nygaard.model.channels.Channel;
import org.seefin.nygaard.model.financial.FinancialTransaction;
import org.seefin.nygaard.model.parties.Organization;


/**
 * Abstract notion of a service (or product) that a supplier offers to a customer segment
 * 
 * @author phillipsr
 *
 */
public interface Service
{
	/**
	 * Execute the transaction with this service
	 * @param channel through which the request was received
	 * @param transaction to be executed
	 * @throws RuntimeException or subclass, detailing any errors encountered 
	 */
	public void execute ( Channel channel, FinancialTransaction transaction);
	
	/** @return the provider of this service */
	Organization getProvider();

	/** @return the product instance, configuring this service */
	public Product getProduct ();

}
