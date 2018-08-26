package org.seefin.nygaard.model.identifiers;

import java.util.List;

/**
 * Representation of a person's name; consisting of a sequence of
 * name elements,
 *
 * @author phillipsr
 */
public final class PersonalName
        implements Identity {
    private final String[] elements;

    /**
     * Create a PersonalName initialized from the name element list provided
     *
     * @param elements of the personal name
     * @throw IllegalArgumentException if elements is null or empty
     */
    public PersonalName(List<String> elements) {
        if (elements == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (elements.isEmpty() == true) {
            throw new IllegalArgumentException("name must have at leats one element");
        }
        this.elements = new String[elements.size()];
        elements.toArray(this.elements);
    }

    /**
     * Create a PersonalName initialized from the name element array provided
     *
     * @param elements of the personal name
     * @throw IllegalArgumentException if elements is null or empty
     */
    public PersonalName(String[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (elements.length == 0) {
            throw new IllegalArgumentException("name must have at leats one element");
        }
        this.elements = new String[elements.length];
        System.arraycopy(elements, 0, this.elements, 0, elements.length);
    }

    /**
     * @return an array containing all elements of the name
     */
    public String[]
    getNameElements() {
        String[] result = new String[elements.length];
        System.arraycopy(elements, 0, result, 0, elements.length);
        return result;
    }

    /**
     * @return the first element of the name
     */
    public String getCommonName() {
        return elements[0];
    }

    @Override
    public String
    toString() {
        return elements.toString();
    }

    @Override
    public String
    externalForm() {
        return toString();
    }
}
