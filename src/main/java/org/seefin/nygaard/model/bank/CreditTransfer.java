package org.seefin.nygaard.model.bank;

import org.seefin.nygaard.model.financial.Account;
import org.seefin.nygaard.model.financial.FinancialTransaction;
import org.seefin.nygaard.model.financial.MonetaryAmount;

/**
 * @author phillipsr
 *
 */
public final class CreditTransfer
	extends FinancialTransaction
{
	private final String text; // the reason for the payment (optional)
	
	/**
	 * @param fromAccount
	 * @param account
	 * @param amount
	 */
	public CreditTransfer ( 
			Account fromAccount, MonetaryAmount amount, Account toAccount)
	{
		super ( fromAccount, amount, toAccount);
		this.text = "";
	}

	public String getText () { return text; }
}
