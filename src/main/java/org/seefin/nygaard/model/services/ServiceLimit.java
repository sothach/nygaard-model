package org.seefin.nygaard.model.services;

/**
 * Enumeration of the possible service limits that may be configure
 * for a service offered by the platform
 * 
 * @author phillipsr
 *
 */
public enum ServiceLimit
{
	ONE_TIME, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, CHANNEL, PER_CUSTOMER, PER_AGENT
}
