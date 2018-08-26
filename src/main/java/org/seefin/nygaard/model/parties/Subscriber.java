package org.seefin.nygaard.model.parties;


public class Subscriber
        extends PartyRole {
    public Subscriber(Individual actor) {
        this.actor = actor;
        this.label = "Subscriber";
    }

    @Override
    public String
    toString() {
        return label + "{" + actor + "}";
    }

}
