package org.seefin.nygaard.model.bank;

import org.seefin.nygaard.model.financial.Instrument;
import org.seefin.nygaard.model.parties.Party;


/**
 * A payment party represents either the payer (debtor) or the payee (creditor,
 * beneficiary) in a payment scenario: this includes both details of the party's
 * identity, and the instrument being used as the source or target of the transfer
 * @author phillipsr
 *
 */
public class PaymentParty
{
	private final Party party;
	private final Instrument instrument;
	
	public PaymentParty ( Party party, Instrument instrument)
	{
		this.party = party;
		this.instrument = instrument;
	}
	
	public Party getParty() { return party; }
	public Instrument getInstrument() { return instrument; }
}
