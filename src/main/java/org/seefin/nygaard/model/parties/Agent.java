package org.seefin.nygaard.model.parties;

/**
 * An agent is a party that acts on behalf of another (the <i>principal</i>), an organization;
 * <p/>
 * Expect specializations (not final)
 *
 * @author phillipsr
 */
public class Agent
        extends PartyRole {
    private final Organization principal;

    public Agent(Party agent, Organization principal) {
        if (agent == null) {
            throw new IllegalArgumentException("Agent cannot be null");
        }
        if (principal == null) {
            throw new IllegalArgumentException("Principal cannot be null");
        }
        this.actor = agent;
        this.principal = principal;
    }

    /**
     * @return the organization on whose behalf the agent operates
     */
    public Organization getPrincipal() {
        return principal;
    }

    @Override
    public String
    toString() {
        return "Agent " + actor + " of " + principal;
    }
}
