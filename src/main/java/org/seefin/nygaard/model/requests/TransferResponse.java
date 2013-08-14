package org.seefin.nygaard.model.requests;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

import org.seefin.nygaard.model.financial.MonetaryAmount;

/**
 * Temporary model for development/test - move or replace with canonical version
 * @author phillipsr
 *
 */
public final class TransferResponse
	implements Serializable
{
	private final boolean success;
	private final String authorizationCode;
	private final String text;
    private final MonetaryAmount balance;

	/**
	 * Create a response object for a transfer request
	 * @param success did the transfer succeed?
	 * @param authorizationCode
	 * @param text
	 * @param balance
	 */
	public TransferResponse ( 
			boolean success,  String authorizationCode, String text, MonetaryAmount balance)
	{
		this.success = success;
		this.authorizationCode = authorizationCode;
		this.text = text;
        this.balance  = balance;
	}
	
	public TransferResponse (boolean success,  String authorizationCode, String text)
	{
		this ( success, authorizationCode, text,
				MonetaryAmount.fromBigDecimal ( Currency.getInstance ( "USD"), BigDecimal.ZERO));
	}

	public boolean getSuccess ()
	{
		return success;
	}
	public String getAuthorizationCode()
	{
		return authorizationCode;
	}
	public String getText ()
	{
		return text;
	}
    public MonetaryAmount getBalance() {
        return balance;
    }

    @Override
	public String
	toString()
	{
		return "TopupResponse{status=" + (success ? "success" : "failed") 
				+ ", authCode="+ authorizationCode
				+ ", text=\""+ text + "\"}";
	}

}
