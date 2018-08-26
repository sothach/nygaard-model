package org.seefin.nygaard.model.parties;

import org.seefin.nygaard.model.identifiers.Identity;
import org.seefin.nygaard.model.identifiers.PersonalName;

/**
 * @author phillipsr
 */
public class Individual
        implements Party {
    private PersonalName name;
    private Identity id;

    public Individual(PersonalName name, Identity id) {
        if (name == null) {
            throw new IllegalArgumentException("PersonalName cannot be null");
        }
        if (id == null) {
            throw new IllegalArgumentException("Identity cannot be null");
        }
        this.name = name;
        this.id = id;
    }

    public Individual(String name, Identity id) {
        this(createName(name), id);
    }

    /**
     * @param name
     * @return
     */
    private static PersonalName
    createName(String name) {
        if (name == null || name.isEmpty() == true) {
            throw new IllegalArgumentException("PersonalName cannot be null or blank");
        }
        return new PersonalName(new String[]{name});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCommonName() {
        return name.getCommonName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity getId() {
        return id;
    }

    @Override
    public String
    toString() {
        return this.getCommonName() + "(" + this.getId() + ")";
    }

}
