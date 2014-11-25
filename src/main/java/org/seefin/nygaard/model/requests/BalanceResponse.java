package org.seefin.nygaard.model.requests;

import java.io.Serializable;

import org.seefin.nygaard.model.financial.MonetaryAmount;


/**
 * Temporary model for development/test - move or replace with canonical version
 * @author phillipsr
 *
 */
public class BalanceResponse
	implements Serializable
{
	private MonetaryAmount balance;
	/**
	 * @param success2
	 * @param balance2
	 * @param string
	 */
	public BalanceResponse ( MonetaryAmount balance)
	{
		this.balance = balance;
	}

	public MonetaryAmount getBalance ()
	{
		return balance;
	}
	
	
	@Override
	public String
	toString()
	{
		return "BalanceResponse{balance="+ balance + "}";
	}

}
