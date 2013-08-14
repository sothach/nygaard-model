package org.seefin.nygaard.model.requests;

import org.seefin.nygaard.model.channels.Channel;


/**
 * @author phillipsr
 *
 */
public class ListAccountRequest
	extends Request
{

	/**
	 * @param channel
	 */
	protected ListAccountRequest ( Channel channel)
	{
		super ( channel);
		// TODO Auto-generated constructor stub
	}
	// TODO: may need to be able to specify account types, etc.?
}
