package org.seefin.nygaard.model.channels;

import java.io.Serializable;

import org.seefin.nygaard.model.identifiers.Identity;
import org.seefin.nygaard.model.parties.PartyRole;


/**
 * Representation of a channel via which a {@link Request} has been received<p/>
 * This may describe a physical channel, such as an SMS gateway, or logical, such as
 * a named access channel; expect channel names may be defined as paths, representing
 * sub-channels, etc. (TODO: how? what is the relationship between the channel id and
 * it's name/path?)
 * <p/>
 * The role of channels is primarily for routing(*), validation and limit
 * checking, but is also useful for audit and logging purposes
 * <p/>
 * (*) For multitenancy scenarios, we need to be able to decide how to route a request;
 * by examining the channel information, particularly <code>agent</code>, we can decide
 * on whose behalf a request should be processed, and route accordingly (e.g., select
 * the correct business process or service instance - <i>Saga</i>)
 *
 * @author phillipsr
 */
public class Channel
        implements Serializable {
    private final PartyRole agent;
    private final Identity reference;

    /**
     * Create a new channel, identifying the agent that is submitting requests
     *
     * @param agent     that is submitting requests through this channel
     * @param reference unique ID identifying the channel in the platform
     */
    public Channel(PartyRole agent, Identity reference) {
        if (agent == null) {
            throw new IllegalArgumentException("Channel agent must be provided");
        }
        this.agent = agent;
        if (reference == null) {
            throw new IllegalArgumentException("Channel reference must be provided");
        }
        this.reference = reference;
    }

    /**
     * @return the agent that submitted the request
     */
    public PartyRole getAgent() {
        return agent;
    }

    /**
     * @return the unique ID of the channel in the platform
     */
    public Identity getIdentity() {
        return reference;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String
    toString() {
        return getName() + "{agent=" + agent + ", id=" + reference + "}";
    }
}
