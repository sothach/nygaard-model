package org.seefin.nygaard.model.parties;

public class Customer
        extends PartyRole {
    private final Supplier supplier;

    public Customer(Party actor, Supplier supplier) {
        if (actor == null) {
            throw new IllegalArgumentException("Actor cannot be null");
        }
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        this.actor = actor;
        this.supplier = supplier;
    }

    /**
     * @return the supplier providing the goods/services to this customer
     */
    public Supplier getSupplier() {
        return supplier;
    }
}
