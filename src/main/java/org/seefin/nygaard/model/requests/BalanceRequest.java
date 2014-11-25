package org.seefin.nygaard.model.requests;

import org.seefin.nygaard.model.channels.Channel;
import org.seefin.nygaard.model.financial.Instrument;


/**
 * This request bean represents the normalized concept of a balance inquiry request
 * 
 * @author phillipsr
 *
 */
public final class BalanceRequest
	extends Query
{
	private final Instrument instrument;

	/**
	 * Create a balance request, initiated by <code>requestor</code> for
	 * the supplied <code>instrument</code>
	 * @param requestor making the request
	 * @param instrument whose balance is being queried
	 */
	public BalanceRequest ( Channel channel, Instrument instrument)
	{
		super ( channel);
		if ( instrument == null)
		{
			throw new IllegalArgumentException ("Instrument may not be null");
		}
		this.instrument = instrument;
	}
	
	/** @return the instrument for which the balance is being requested */
	public Instrument getInstrument() { return instrument; }
	
	@Override
	public String
	toString()
	{
		return this.getClass ().getSimpleName () 
				+ "{Channel=" + this.getChannel() + ",Instrument=" + instrument + "}";
	}

}
