package org.seefin.nygaard.model.channels;

import java.net.InetAddress;

import org.seefin.nygaard.model.parties.PartyRole;


/**
 * @author phillipsr
 *
 */
public class WebChannel
	extends Channel
{
	private InetAddress address;

	/**
	 * @param agent
	 * @param address
	 */
	public WebChannel ( PartyRole agent, InetAddress address)
	{
		super ( agent, agent.getActor(). getId ());
		this.address = address;
	}
	
	public InetAddress getAddress() { return address; }
	
	@Override
	public String
	toString()
	{
		return super.toString() + ", address=" + address;
	}

}
