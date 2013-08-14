package org.seefin.nygaard.model.financial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

import org.seefin.nygaard.model.identifiers.AccountNumber;
import org.seefin.nygaard.model.parties.Organization;
import org.seefin.nygaard.model.parties.Party;


/**
 * Abstract notion of a financial account; a record of transactions
 * performed using one or more <code>Instrument<code>s
 * 
 * @author phillipsr
 *
 */
public abstract class Account
	implements Serializable
{
	public enum AccountState
	{
		CREATED, OPEN, BLOCKED, CLOSED
	}
	private final Party owner;
	private final AccountNumber accountId;
	private final Organization provider;
	private MonetaryAmount balance;
	private AccountState state;
	
	/**
	 * Create a new account instance
	 * @param owner of the new account
	 * @param provider of the account service
	 * @param accountCurrency of the account balance, etc.
	 * @param accountId uniquely identifying this account
	 * @throws IllegalArgumentException if any of the parameters are null
	 */
	protected Account ( Party owner, Organization provider, Currency accountCurrency, AccountNumber accountId)
	{
		if ( owner == null)
		{
			throw new IllegalArgumentException ( "Account owner may not be null");
		}
		if ( provider == null)
		{
			throw new IllegalArgumentException ( "Account provider may not be null");
		}
		if ( accountCurrency == null)
		{
			throw new IllegalArgumentException ( "Account currency owner may not be null");
		}
		if ( accountId == null)
		{
			throw new IllegalArgumentException ( "Account ID owner may not be null");
		}
        this.accountId = accountId;
		this.owner = owner;
		this.provider = provider;
		this.balance = MonetaryAmount.fromBigDecimal ( accountCurrency, BigDecimal.ZERO);
		state = AccountState.CREATED;
	}
	
	/** @return the party holding this account (the customer) */
	public Party getHolder() { return owner; }
	
	/** @return
	 * 		The unique identity of this account; it may be an account number, such
	 * 		as an IBAN, identifying a bank account, or an identifier for an eWallet
	 */
	public AccountNumber getIdentifier () { return accountId; }
	
	/** @return Institution providing the account services */
	public Organization getProvider () { return provider; }
	
	/** @return the current balance for this wallet */
	public MonetaryAmount getBalance() { return balance; }
	
	/** @return the currency that this account transacts in */ 
	public Currency getAccountCurrency () { return balance.getCurrency (); }
	
	/** @return the state of this account */
	public AccountState getState() { return state; }
	
	/**
	 * Update the account balance 
	 * @param newBalance amount to set the balance to
	 * @throws IllegalArgumentException if newBalance is null, 
	 * 			or newBalance.getCurrency() not getCurrency() 
	 */
	public void 
	setBalance ( MonetaryAmount newBalance)
	{
		if ( newBalance == null)
		{
			throw new IllegalArgumentException ( "New balance cannot be null");
		}
		if ( balance.getCurrency ().equals ( newBalance.getCurrency ()) == false)
		{
			throw new IllegalArgumentException ( 
					"Currency mismatch, expected=" + balance.getCurrency () + ", got=" + newBalance.getCurrency ());
		}
		balance = newBalance;
	}
	
	@Override
	public String
	toString()
	{
		return this.getClass ().getSimpleName () 
				+ "{owner=" + owner + ",number="+accountId+",provider=" + provider + "}";
	}
	
}
