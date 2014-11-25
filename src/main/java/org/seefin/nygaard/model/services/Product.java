package org.seefin.nygaard.model.services;

import org.seefin.nygaard.model.channels.Channel;
import org.seefin.nygaard.model.financial.MonetaryAmount;
import org.seefin.nygaard.model.financial.Transaction;


/**
 * @author phillipsr
 *
 */
public interface Product
{
	/** @return the value of the configured limit, for the product */ 
	public MonetaryAmount getLimit ( ServiceLimit limit);

	/**
	 * Validate the supplied transaction in terms of this product's configuration
	 * @param channel from which the transaction originated
	 * @param transaction to be validated
	 * @return null if the transaction is valid, otherwise return an exception
	 * 			that the caller can examine and/or throw back up the stack
	 */
	public RuntimeException checkTransaction ( Channel channel, Transaction transaction);
}
