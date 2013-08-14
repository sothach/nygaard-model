package org.seefin.nygaard.model.requests;

import org.seefin.nygaard.model.channels.Channel;
import org.seefin.nygaard.model.collections.WormMap;
import org.seefin.nygaard.model.financial.MonetaryAmount;
import org.seefin.nygaard.model.parties.Subscriber;


/**
 * This request bean represents the normalized concept of a topup request
 * <p/>
 * Note, this should be an immutable value object (i.e., all fields final), but the
 * requirements of the adapter mapping (Dozer) require that it can be created via
 * a default (no arg) constructor; for this reason, it should be used and discarded 
 * in the minimum scope possible
 * 
 * @author phillipsr
 *
 */
public final class TopupRequest
	extends Command
{
	private final MonetaryAmount amount;
	private final Subscriber subscriber;
	/**
	 * Information that is not used by MP, and is attached the request, for
	 * possible processing / transmission by back-end systems
	 */
	private WormMap<String,Object> supplementaryData;
	
	public TopupRequest ( Channel channel, Subscriber subscriber, MonetaryAmount amount)
	{
		super ( channel);
		if ( subscriber == null)
		{
			throw new IllegalArgumentException ( "Subscriber cannot be null");
		}
		if ( amount == null)
		{
			throw new IllegalArgumentException ("Amount may not be null");
		}
		this.amount = amount;
		this.subscriber = subscriber;
	}
	
	public MonetaryAmount getAmount () { return amount; }
	public Subscriber getSubscriber () { return subscriber; }
	public WormMap<String,Object> getSupplementaryData () { return supplementaryData; }
	
	@Override
	public String
	toString()
	{
		return this.getClass ().getSimpleName () 
				+ "{Amount: " + amount + ", Channel: " + this.getChannel() + "}";
	}

}
