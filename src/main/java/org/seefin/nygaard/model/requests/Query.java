package org.seefin.nygaard.model.requests;

import org.seefin.nygaard.model.channels.Channel;

/**
 * @author phillipsr
 */
public abstract class Query
        extends Request {

    /**
     * @param channel
     */
    protected Query(Channel channel) {
        super(channel);
    }

}
