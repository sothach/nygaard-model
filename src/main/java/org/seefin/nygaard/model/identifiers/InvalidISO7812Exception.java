package org.seefin.nygaard.model.identifiers;

/**
 * Exception thrown when attempting to construct an ISO7812 identifier, when there is no
 * scheme registered matching the number implied, or the subsequent parts do not conform
 * to the registered scheme, or the checksum is incorrect
 *
 * @author phillipsr
 */
public class InvalidISO7812Exception
        extends RuntimeException {
    private final String errorPart;
    private final String number;
    private final String reason;

    public InvalidISO7812Exception(String number, String reason, String errorPart) {
        super("Invalid ISO7812 for " + number + ": " + reason + " (" + errorPart + ")");
        this.number = number;
        this.errorPart = errorPart;
        this.reason = reason;
    }

    public InvalidISO7812Exception(String number, String reason) {
        super("Invalid ISO7812 for " + number + ": " + reason);
        this.number = number;
        this.errorPart = "";
        this.reason = reason;
    }

    public String getErrorPart() {
        return errorPart;
    }

    public String getNumber() {
        return number;
    }

    public String getReason() {
        return reason;
    }
}
