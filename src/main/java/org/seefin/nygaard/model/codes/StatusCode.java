package org.seefin.nygaard.model.codes;

import java.io.Serializable;

/**
 * Normalized response and error codes used in communication between MP and ARC
 * (should move out of arc-common to common-model)
 * <ul>
 * <li>Numeric, "speaking" codes</li>
 * <li>Represented by positive, non-zero integers</li>
 * <li>Specifies: Zone, Module, Component and Response</li>
 * <li>8-digit scheme allows for 9 zones, 99 modules-per-zone, 99 components and 1000 response codes</li>
 * </ul>
 * Example:
 * <ul>
 * <li>Zone #2 (Out-bound rail, back-end)</li>
 * <li>Module 10</li>
 * <li>Component 24</li>
 * <li>Error code 126</li>
 * </ul>
 * <pre>
 * RMMCCRRR
 * 21024126 or, formatted, 210-24-126
 * </pre>
 *
 * @author phillipsr
 */
public final class StatusCode
        implements Comparable<StatusCode>, Serializable {
    public static final int ZONE_INBOUND = 1;
    public static final int ZONE_OUTBOUND = 2;
    public static final int ZONE_FOH_SERVICE = 3;
    public static final int ZONE_APP_SERVICES = 4;
    public static final int ZONE_DOMAIN_SERVICES = 5;

    private static final int COMPONENT_FACTOR = 1000;
    private static final int MODULE_FACTOR = 100000;
    private static final int ZONE_FACTOR = 10000000;
    private final int code;

    private StatusCode(int code) {
        this.code = code;
        int zone = getZone();
        if (zone <= 0 || zone > 9) {
            throw new IllegalArgumentException("zone# must be 1 .. 9 not " + zone);
        }
        int module = getModule();
        if (module <= 0 || module > 99) {
            throw new IllegalArgumentException("module# must be 1 .. 99 not " + module);
        }
        int component = getComponent();
        if (component <= 0 || component > 99) {
            throw new IllegalArgumentException("component# must be 1 .. 99 not " + component);
        }
        int response = getResponse();
        if (response < 0 || response > 999) {
            throw new IllegalArgumentException("response code must be 0 .. 999 not " + response);
        }
    }

    public static int
    createLocation(int zone, int module, int component) {
        return (zone * (ZONE_FACTOR / COMPONENT_FACTOR))
                + (module * (MODULE_FACTOR / COMPONENT_FACTOR))
                + component;
    }

    /**
     * Create a status code object from it's component codes
     *
     * @param zone      on which the module is running
     * @param module    providing the service
     * @param component of the module involved
     * @param response  code from the component
     * @return StatusCode initialized from supplied parameters
     */
    public static StatusCode
    createCode(int zone, int module, int component, ResponseCode response) {
        return createCode(zone, module, component, response.getCode());
    }

    /**
     * Create a status code from the pre-assembled location plus a response code
     *
     * @param location is zone + module + component
     * @param response code to append to create full code
     * @return
     */
    public static StatusCode
    createCode(int location, ResponseCode response) {
        return new StatusCode(location * COMPONENT_FACTOR + response.getCode());
    }

    /**
     * Create a status code object from it's component codes
     *
     * @param zone      in which the module is running
     * @param module    providing the service
     * @param component of the module involved
     * @param response  code from the component, as an integer
     * @return StatusCode initialized from supplied parameters
     */
    public static StatusCode
    createCode(int zone, int module, int component, int response) {
        if (zone <= 0 || zone > 9) {
            throw new IllegalArgumentException("Zone# must be 1 .. 9 not " + zone);
        }
        if (module <= 0 || module > 99) {
            throw new IllegalArgumentException("module# must be 1 .. 99 not " + module);
        }
        if (component <= 0 || component > 99) {
            throw new IllegalArgumentException("component# must be 1 .. 99 not " + component);
        }
        if (response < 0 || response > 999) {
            throw new IllegalArgumentException("response code must be 0 .. 999 not " + response);
        }
        return new StatusCode(
                (zone * ZONE_FACTOR) + (module * MODULE_FACTOR)
                        + (component * COMPONENT_FACTOR) + response);
    }

    /**
     * Create a status code object from its integer representation
     *
     * @param code as an integer
     * @return StatusCode initialized from supplied <code>code</code>
     */
    public static StatusCode
    createCode(int code) {
        return new StatusCode(code);
    }

    /**
     * Answer with the numeric response code
     */
    public int intValue() {
        return code;
    }

    /**
     * Answer with the Rail part of the response code
     */
    public int getZone() {
        return code / ZONE_FACTOR;
    }

    /**
     * Answer with the Module part of the response code
     */
    public int getModule() {
        return code / MODULE_FACTOR - 100 * getZone();
    }

    /**
     * Answer with the Component part of the response code
     */
    public int getComponent() {
        return code / COMPONENT_FACTOR - 100 * (getModule() + 100 * getZone());
    }

    /**
     * Answer with the Response part of the response code
     */
    public int getResponse() {
        return code - (code / 1000) * 1000;
    }

    @Override
    public String
    toString() {
        return String.format("%01d%02d-%02d-%03d",
                getZone(), getModule(), getComponent(), getResponse());
    }


    @Override
    public boolean
    equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        return code == ((StatusCode) other).code;
    }

    @Override
    public int
    hashCode() {
        return code;
    }

    @Override
    public int
    compareTo(StatusCode other) {
        return code - other.code;
    }

}
