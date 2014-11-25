package org.seefin.nygaard.model.channels;

import org.seefin.nygaard.model.identifiers.DUNS;
import org.seefin.nygaard.model.identifiers.Identity;
import org.seefin.nygaard.model.parties.Agent;
import org.seefin.nygaard.model.parties.Organization;


/**
 * Channel representing requests originated by the platform,
 * and routed to ARC
 * 
 * @author phillipsr
 *
 */
public class MPChannel
	extends Channel
{
	private static final String VENDOR_NAME = "CyprusMFS";
	private static final String VENDOR_DUNS = "039189524";

	public MPChannel ( Identity transactionId)
	{
		super ( createChannelAgent(), transactionId);
	}
	
	private static Agent
	createChannelAgent()
	{
		Organization organization = new Organization(DUNS.parse ( VENDOR_DUNS), VENDOR_NAME);
		return new Agent ( organization, organization);
	}

}
