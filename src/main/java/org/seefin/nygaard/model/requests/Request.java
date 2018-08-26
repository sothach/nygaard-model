package org.seefin.nygaard.model.requests;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.seefin.nygaard.model.channels.Channel;


/**
 * Generic representation of a request made by MP to an external system,
 * describing the common aspects that should be present in all requests
 * <p/>
 * Requests are effectively the Command objects of CQRS (just more polite),
 * and will always be associate with a {@link Channel}, detailing the source
 * of the request
 *
 * @author phillipsr
 */
public abstract class Request
        implements Serializable {
    /**
     * Details of where the request came from, this includes the party
     * making the request, maybe the end customer, or an intermediate
     * agent, on the customer's behalf
     */
    private final Channel channel;
    private final DateTime timestamp;

    /**
     * Instantiate a request coming from the specified <code>channel</code>
     *
     * @param channel thru which the request was received
     */
    protected Request(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel may not be null");
        }
        this.channel = channel;
        this.timestamp = new DateTime();
    }

    /**
     * @return the channel from which the request was received
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @return the time at which the request was first created
     */
    public DateTime getTimestamp() {
        return timestamp;
    }
}
