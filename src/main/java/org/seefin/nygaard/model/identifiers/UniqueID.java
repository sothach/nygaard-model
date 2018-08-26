package org.seefin.nygaard.model.identifiers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.UID;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A globally-unique ID value, that uses a base 36 (<code>Character.MAX_RADIX</code>)
 * compact external String format
 * <p/>
 * Database: the generated ID can be stored in a database a NUMBER(17,0) column
 * <p/>
 * Warning: these IDs are only unique for a single JVM/Host combination, consider using UUID for
 * universally unique values
 *
 * @author phillipsr
 */
public final class UniqueID
        implements Identity, Comparable<UniqueID> {
    private static final SecureRandom randomizer = new SecureRandom();
    private static final int EXT_RADIX = 36; // current MAX_RADIX
    private static final int EXT_LENGTH = 18;
    private static final Pattern InsignificantChars = Pattern.compile("[^A-Z0-9]");
    private static final Matcher UniqueIDMatcher = Pattern.compile("[A-Za-z0-9]{18}").matcher("");
    private static final Matcher CanonicalUniqueID
            = Pattern.compile("([A-Za-z0-9]{4})-([A-Za-z0-9]{4})-([A-Za-z0-9]{4})-([A-Za-z0-9]{4})-([A-Za-z0-9]{2})").matcher("");

    private final String id;

    private UniqueID() {
        this.id = createUniqueId();
    }

    /**
     * Create an external Id from its externalized String form
     *
     * @param extId to initialize this ID from
     * @throws IllegalArgumentException if <code>extId</code> is null,
     *                                  or is not a string of 16 characters (excluding
     *                                  punctuation, which will be first stripped out)
     */
    private UniqueID(String extId) {
        if (extId == null) {
            throw new IllegalArgumentException("External ID may not be null");
        }
        Matcher matcher = InsignificantChars.matcher(extId.toUpperCase());
        String externalId = matcher.replaceAll("");
        if (externalId.length() != EXT_LENGTH) {
            throw new IllegalArgumentException("External ID must be " + EXT_LENGTH + " in length (got " + externalId.length() + ")");
        }
        this.id = externalId;
    }

    /**
     * factory method to create a new, unique identity code
     *
     * @return
     */
    public static UniqueID
    createUnqiueId() {
        return new UniqueID();
    }

    public static boolean
    isValid(String anIdString) {
        return anIdString != null && anIdString.isEmpty() == false
                && (UniqueIDMatcher.reset(anIdString).matches()
                || CanonicalUniqueID.reset(anIdString).matches());
    }

    /**
     * factory method to create a unique identity code from the supplied String
     *
     * @param extId to initialize this ID from
     * @throws IllegalArgumentException if <code>extId</code> is null,
     *                                  or is not a string of 16 characters (excluding
     *                                  punctuation, which will be first stripped out)
     */
    public static UniqueID
    valueOf(String extId) {
        return new UniqueID(extId);
    }

    public int getIdLength() {
        return EXT_LENGTH;
    }

    /**
     * Convert an external ID form string into the corresponding unique ID
     *
     * @param extId a string with 25 base 36 digits (may contain formatting
     *              characters not in the set "A-Za-z0-9")
     * @return
     */
    public static UniqueID
    fromString(String extId) {
        return new UniqueID(extId);
    }


    /**
     * Answer with a quasi-unique 16 character alphanumeric
     * ID string<p/>
     * This is generated as a concatenation of the current system's
     * IPv4 address plus the current time in milliseconds
     * <p/>
     * Method contains synchronized block to minimize chances of two threads
     * creating the same ID (i.e., at the exact same millisecond)
     *
     * @return
     */
    private static String
    createUniqueId() {
        long ip = ipToLong("127.0.0.1"); // default value
        try {
            ip = ipToLong(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) { /* default to localhost */ }
        String part1 = asRadixPaddedLeft(ip, 7);
        UID uid = new UID();
        long uidVal = Math.abs(uid.hashCode());
        return asRadixPaddedLeft(random(), 1) + part1 + asRadixPaddedLeft(uidVal, 6) + asRadixPaddedLeft(random(), 4);
    }

    private static int
    random() {
        return Math.abs(randomizer.nextInt());
    }

    /**
     * convert value to an upper-case string in base EXT_RADIX
     * (36), and left-pad it with zeros to the width specified
     *
     * @param value
     * @param width
     * @return
     */
    private static String
    asRadixPaddedLeft(long value, int width) {
        return String.format(
                "%" + width + "." + width + "s",
                Long.toString(value, EXT_RADIX).toUpperCase())
                .replaceAll(" ", "0");
    }

    public static long
    ipToLong(String ipAddress) {
        long result = 0;
        String[] parts = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {
            result |= (Long.parseLong(parts[3 - i]) << (i * 8));
        }

        return result & 0xFFFFFFFF;
    }

    @Override
    public String
    toString() {
        return this.id;
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
        return id.equals(((UniqueID) other).id);
    }

    @Override
    public int
    hashCode() {
        return id.hashCode();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int
    compareTo(UniqueID otherId) {
        return id.compareTo(otherId.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String externalForm() {
        return toString();
    }

}
