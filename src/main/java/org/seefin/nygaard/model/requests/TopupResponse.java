package org.seefin.nygaard.model.requests;

import java.io.Serializable;

import org.seefin.nygaard.model.financial.MonetaryAmount;


/**
 * Temporary model for development/test - move or replace with canonical version
 * @author phillipsr
 *
 */
public class TopupResponse
	implements Serializable
{
	private boolean success;
	private MonetaryAmount balance;
	private String text;
	
	/**
	 * Create a empty topup response
	 */
	public TopupResponse()
	{
	}

	/**
	 * @param success2
	 * @param balance2
	 * @param string
	 */
	public TopupResponse ( 
			boolean success, MonetaryAmount balance, String text)
	{
		this.success = success;
		this.balance = balance;
		this.text = text;
	}

	public boolean getSuccess ()
	{
		return success;
	}
	public void setSuccess ( boolean success)
	{
		this.success = success;
	}
	public MonetaryAmount getBalance ()
	{
		return balance;
	}
	public void setBalance ( MonetaryAmount balance)
	{
		this.balance = balance;
	}
	public String getText ()
	{
		return text;
	}
	public void setText ( String text)
	{
		this.text = text;
	}
	
	
	@Override
	public String
	toString()
	{
		return "TopupResponse{status=" + (success ? "success" : "failed") 
				+ ", balance="+ balance
				+ ", text=\""+ text + "\"}";
	}

}
