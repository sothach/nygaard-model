package org.seefin.nygaard.model.financial;

import org.seefin.nygaard.model.identifiers.Identity;
import org.seefin.nygaard.model.parties.Party;


/**
 * A financial instrument is a tradeable asset of any kind; either cash, evidence of an ownership 
 * interest in an entity, or a contractual right to receive or deliver cash or another financial instrument.
 * <p/>
 * In the context of MFS, Instrument is a generic view of accounts, wallets, credit cards, etc.
 * 
 * @author phillipsr
 *
 */
public abstract class Instrument
{ 
	/**
	 * @return the owning party of this instrument
	 */
	public abstract Party getHolder ();

	/**
	 * @return
	 * The unique identity of this instrument; it may be an account number, such as
	 * a IBAN, identifying a bank account, or an identifier for an eWallet
	 */
	public abstract Identity getIdentifier ();

}
