package org.seefin.nygaard.model.identifiers;

import org.seefin.nygaard.model.locations.ISO3166;

/**
 * Exception thrown when attempting to construct an IBAN identifier, when there is no
 * scheme registered matching the country implied, or the subsequent parts do not conform
 * to the registered scheme, or the checksum is incorrect
 * @author phillipsr
 *
 */
public class InvalidIBANException
	extends RuntimeException
{
	private final String errorPart;
	private final ISO3166 country;
	private final String reason;
	
	public InvalidIBANException ( ISO3166 country, String reason, String errorPart)
	{
		super ( "Invalid IBAN for " + country + ": " + reason + " (" + errorPart + ")");
		this.country = country;
		this.errorPart = errorPart;
		this.reason = reason;
	}
	
	public String getErrorPart() { return errorPart; }
	public ISO3166 getCountryCode() { return country; }
	public String getReason() { return reason; }
}
