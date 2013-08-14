package org.seefin.nygaard.model.instruments;

import java.io.Serializable;

import org.seefin.nygaard.model.financial.Instrument;
import org.seefin.nygaard.model.identifiers.AccountNumber;
import org.seefin.nygaard.model.parties.Party;


/**
 * @author phillipsr
 *
 */
public class AccountInstrument
	extends Instrument
	implements Serializable
{
	private Party holder;
	private AccountNumber accountNumber;
	
	public AccountInstrument  ( Party owner, AccountNumber accountNumber)
	{
		this.holder = owner;
		this.accountNumber = accountNumber;
	}
	
	@Override
	public Party getHolder() { return holder; }
	@Override
	public AccountNumber getIdentifier () { return accountNumber; }
	
	@Override
	public String
	toString()
	{
		return this.getClass ().getSimpleName () + "{owner=" + holder + ",number="+accountNumber+"}";
	}
}
