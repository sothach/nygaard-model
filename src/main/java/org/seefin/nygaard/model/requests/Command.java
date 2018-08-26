package org.seefin.nygaard.model.requests;

import org.seefin.nygaard.model.channels.Channel;

/**
 * @author phillipsr
 */
public abstract class Command
        extends Request {

    /**
     * @param channel
     */
    protected Command(Channel channel) {
        super(channel);
    }

}
