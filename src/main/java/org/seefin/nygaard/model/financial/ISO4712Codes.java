package org.seefin.nygaard.model.financial;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * ISO4712 Currency code table
 * <p/>
 * work-around for pre-1.7 java code that needs the numeric ISO4172 value
 * (also provides reverse look-up from numeric code, needed for j7 too)
 * @author phillipsr
 *
 */
public final class ISO4712Codes
{
	public static final Map<String,Integer> Iso4217map = new HashMap<>();
	static
	{
		Iso4217map.put("AED",784);
		Iso4217map.put("AFN",971);
		Iso4217map.put("ALL",  8);
		Iso4217map.put("AMD", 51);
		Iso4217map.put("ANG",532);
		Iso4217map.put("AOA",973);
		Iso4217map.put("ARS", 32);
		Iso4217map.put("AUD", 36);
		Iso4217map.put("AWG",533);
		Iso4217map.put("AZN",944);
		Iso4217map.put("BAM",977);
		Iso4217map.put("BBD", 52);
		Iso4217map.put("BDT", 50);
		Iso4217map.put("BGN",975);
		Iso4217map.put("BHD", 48);
		Iso4217map.put("BIF",108);
		Iso4217map.put("BMD", 60);
		Iso4217map.put("BND", 96);
		Iso4217map.put("BOB", 68);
		Iso4217map.put("BOV",984);
		Iso4217map.put("BRL",986);
		Iso4217map.put("BSD", 44);
		Iso4217map.put("BTN", 64);
		Iso4217map.put("BWP", 72);
		Iso4217map.put("BYR",974);
		Iso4217map.put("BZD", 84);
		Iso4217map.put("CAD",124);
		Iso4217map.put("CDF",976);
		Iso4217map.put("CHE",947);
		Iso4217map.put("CHF",756);
		Iso4217map.put("CHW",948);
		Iso4217map.put("CLF",990);
		Iso4217map.put("CLP",152);
		Iso4217map.put("CNY",156);
		Iso4217map.put("COP",170);
		Iso4217map.put("COU",970);
		Iso4217map.put("CRC",188);
		Iso4217map.put("CUC",931);
		Iso4217map.put("CUP",192);
		Iso4217map.put("CVE",132);
		Iso4217map.put("CZK",203);
		Iso4217map.put("DJF",262);
		Iso4217map.put("DKK",208);
		Iso4217map.put("DOP",214);
		Iso4217map.put("DZD", 12);
		Iso4217map.put("EGP",818);
		Iso4217map.put("ERN",232);
		Iso4217map.put("ETB",230);
		Iso4217map.put("EUR",978);
		Iso4217map.put("FJD",242);
		Iso4217map.put("FKP",238);
		Iso4217map.put("GBP",826);
		Iso4217map.put("GEL",981);
		Iso4217map.put("GHS",936);
		Iso4217map.put("GIP",292);
		Iso4217map.put("GMD",270);
		Iso4217map.put("GNF",324);
		Iso4217map.put("GTQ",320);
		Iso4217map.put("GYD",328);
		Iso4217map.put("HKD",344);
		Iso4217map.put("HNL",340);
		Iso4217map.put("HRK",191);
		Iso4217map.put("HTG",332);
		Iso4217map.put("HUF",348);
		Iso4217map.put("IDR",360);
		Iso4217map.put("ILS",376);
		Iso4217map.put("INR",356);
		Iso4217map.put("IQD",368);
		Iso4217map.put("IRR",364);
		Iso4217map.put("ISK",352);
		Iso4217map.put("JMD",388);
		Iso4217map.put("JOD",400);
		Iso4217map.put("JPY",392);
		Iso4217map.put("KES",404);
		Iso4217map.put("KGS",417);
		Iso4217map.put("KHR",116);
		Iso4217map.put("KMF",174);
		Iso4217map.put("KPW",408);
		Iso4217map.put("KRW",410);
		Iso4217map.put("KWD",414);
		Iso4217map.put("KYD",136);
		Iso4217map.put("KZT",398);
		Iso4217map.put("LAK",418);
		Iso4217map.put("LBP",422);
		Iso4217map.put("LKR",144);
		Iso4217map.put("LRD",430);
		Iso4217map.put("LSL",426);
		Iso4217map.put("LTL",440);
		Iso4217map.put("LVL",428);
		Iso4217map.put("LYD",434);
		Iso4217map.put("MAD",504);
		Iso4217map.put("MDL",498);
		Iso4217map.put("MGA",969);
		Iso4217map.put("MKD",807);
		Iso4217map.put("MMK",104);
		Iso4217map.put("MNT",496);
		Iso4217map.put("MOP",446);
		Iso4217map.put("MRO",478);
		Iso4217map.put("MUR",480);
		Iso4217map.put("MVR",462);
		Iso4217map.put("MWK",454);
		Iso4217map.put("MXN",484);
		Iso4217map.put("MXV",979);
		Iso4217map.put("MYR",458);
		Iso4217map.put("MZN",943);
		Iso4217map.put("NAD",516);
		Iso4217map.put("NGN",566);
		Iso4217map.put("NIO",558);
		Iso4217map.put("NOK",578);
		Iso4217map.put("NPR",524);
		Iso4217map.put("NZD",554);
		Iso4217map.put("OMR",512);
		Iso4217map.put("PAB",590);
		Iso4217map.put("PEN",604);
		Iso4217map.put("PGK",598);
		Iso4217map.put("PHP",608);
		Iso4217map.put("PKR",586);
		Iso4217map.put("PLN",985);
		Iso4217map.put("PYG",600);
		Iso4217map.put("QAR",634);
		Iso4217map.put("RON",946);
		Iso4217map.put("RSD",941);
		Iso4217map.put("RUB",643);
		Iso4217map.put("RWF",646);
		Iso4217map.put("SAR",682);
		Iso4217map.put("SBD", 90);
		Iso4217map.put("SCR",690);
		Iso4217map.put("SDG",938);
		Iso4217map.put("SEK",752);
		Iso4217map.put("SGD",702);
		Iso4217map.put("SHP",654);
		Iso4217map.put("SLL",694);
		Iso4217map.put("SOS",706);
		Iso4217map.put("SRD",968);
		Iso4217map.put("SSP",728);
		Iso4217map.put("STD",678);
		Iso4217map.put("SYP",760);
		Iso4217map.put("SZL",748);
		Iso4217map.put("THB",764);
		Iso4217map.put("TJS",972);
		Iso4217map.put("TMT",934);
		Iso4217map.put("TND",788);
		Iso4217map.put("TOP",776);
		Iso4217map.put("TRY",949);
		Iso4217map.put("TTD",780);
		Iso4217map.put("TWD",901);
		Iso4217map.put("TZS",834);
		Iso4217map.put("UAH",980);
		Iso4217map.put("UGX",800);
		Iso4217map.put("USD",840);
		Iso4217map.put("USN",997);
		Iso4217map.put("USS",998);
		Iso4217map.put("UYI",940);
		Iso4217map.put("UYU",858);
		Iso4217map.put("UZS",860);
		Iso4217map.put("VEF",937);
		Iso4217map.put("VND",704);
		Iso4217map.put("VUV",548);
		Iso4217map.put("WST",882);
		Iso4217map.put("XAF",950);
		Iso4217map.put("XAG",961);
		Iso4217map.put("XAU",959);
		Iso4217map.put("XBA",955);
		Iso4217map.put("XBB",956);
		Iso4217map.put("XBC",957);
		Iso4217map.put("XBD",958);
		Iso4217map.put("XCD",951);
		Iso4217map.put("XDR",960);
		Iso4217map.put("XFU",  0);
		Iso4217map.put("XPF",953);
		Iso4217map.put("XPT",962);
		Iso4217map.put("XTS",963);
		Iso4217map.put("XXX",999);
		Iso4217map.put("YER",886);
	}

	private static Map<Integer,Currency> numericMap = new HashMap<>();
	
	/**
	 * Answer with Currency instance having the numeric code supplied
	 * @param currencyCode ISO4172 numeric currency code
	 * @return matching Currency instance, or null if not found
	 */
	public static Currency
	getCurrency ( int currencyCode)
	{
		Currency result = numericMap.get ( currencyCode);
		if ( result != null)
		{
			return result;
		}
		/* Java7 code
	    for(Currency c : Currency.getAvailableCurrencies())
	    {
	        if ( c.getNumericCode() == currencyCode)
	        {
	        	numericMap.put ( currencyCode, c);
	            return c;
	        }
	    }
	    */
		for ( Map.Entry<String, Integer> item : Iso4217map.entrySet ())
	    {
	        if ( item.getValue() == currencyCode)
	        {
	        	result = Currency.getInstance ( item.getKey ());
	        	numericMap.put ( currencyCode, result);
	            return result;
	        }
	    }
	    return null;
	}
}
