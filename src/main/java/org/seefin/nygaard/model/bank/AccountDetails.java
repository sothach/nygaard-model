package org.seefin.nygaard.model.bank;

import org.seefin.nygaard.model.financial.MonetaryAmount;
import org.seefin.nygaard.model.identifiers.AccountNumber;


/**
 * @author phillipsr
 *
 */
public class AccountDetails
{
	private AccountNumber number;

	private AccountType type;
	private AccountStatus status;
	
	/** The ledger balance is found by subtracting the total number of debits from
	 *  the total number of credits for a given accounting period. The ledger balance
	 *  is used solely in the reconciliation of book balances. */
	private MonetaryAmount ledgerBalance;
	/** the amount of funds available for withdrawal */
	private MonetaryAmount availableBalance;

	private MonetaryAmount overdraft;
	
	@Override
	public String
	toString()
	{
		return this.getClass ().getSimpleName () + " { acc#=" + number
				+ ", type=" + type + ", status=" + status + ", ledgerBalance=" + ledgerBalance 
				+ ", availableBalance=" + availableBalance + ", overdraft=" + overdraft + "}";
	}

}
