package org.seefin.nygaard.model.identifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.seefin.nygaard.model.identifiers.BBAN;
import org.seefin.nygaard.model.identifiers.IBAN;
import org.seefin.nygaard.model.identifiers.IBANScheme;
import org.seefin.nygaard.model.identifiers.ISO9362;
import org.seefin.nygaard.model.identifiers.InvalidIBANException;
import org.seefin.nygaard.model.identifiers.NL_BBAN;
import org.seefin.nygaard.model.locations.ISO3166;


public class TestIBANSchemes
{
	private static final String[] TestNumbers = 
		{
		"IE86 BOFI 9014 9012 3456 78",		// Ireland
		"GR16 0110 1250 0000 0001 2300 695", // Greece
		"GB29 NWBK 6016 1331 9268 19", 		// United Kingdom
		"SA03 8000 0000 6080 1016 7519", 	// Saudi Arabia
		"CH93 0076 2011 6238 5295 7", 		// Switzerland
		"IL62 0108 0000 0009 9999 999" 		// Israel	
		};
	private static final ISO3166 COUNTRY_IE = ISO3166.valueOf ( "IE");
	
	/*
	 * IE:BANK=c4;BRANCH=n6;ACC=n8
	 */
	@Test
	public void
	testGuessSchemeIE()
	{
		IBAN number = IBAN.parse("IE 64 IRCE 920501 12345678");
		Assert.assertEquals ( "IE64IRCE92050112345678", number.externalForm ());
		Assert.assertEquals ( COUNTRY_IE, number.getCountryCode ());
		Assert.assertEquals ( new BBAN ( "IRCE92050112345678"), number.getBBAN ());
		Assert.assertEquals ( "IRCE", number.getBankCode ());
		Assert.assertEquals ( "920501", number.getBranchCode ());
		Assert.assertEquals ( "12345678", number.getAccountNumber ());
	}
	
	@Test(expected=InvalidIBANException.class)
	public void
	testInvalidIE()
	{
		try
		{
			IBAN.parse("IE64IRCE9205011234567");
		}
		catch ( InvalidIBANException e)
		{
			Assert.assertEquals ( COUNTRY_IE, e.getCountryCode ());
			Assert.assertEquals ( "IBAN string must be 22 characters in length", e.getReason());
			throw e;
		}
	}
	
	@Test(expected=InvalidIBANException.class)
	public void
	testInvalidMod97()
	{
		try
		{
			IBAN.parse("IE64IRCE92050112345679");
		}
		catch ( InvalidIBANException e)
		{
			Assert.assertEquals ( COUNTRY_IE, e.getCountryCode ());
			Assert.assertEquals ( "Mod97 checksum failed", e.getReason());
			throw e;
		}
	}
	
	@Test
	public void
	testUS()
	{
		// US IBAN doesn't exists, but could be constructed from the ABA MICR and the account number:
		// The MICR number is of the form XXXXYYYYC
		// where XXXX is Federal Reserve Routing Symbol, YYYY is ABA Institution Identifier, and C is the Check Digit
		// Routing # Checking Acct. #
		// 212456251 100003456789
		IBAN usIban = IBANScheme.create ( ISO3166.valueOf ( "US"), "212456251", "", "100003456789");
		Assert.assertEquals ( "US97212456251100003456789", usIban.externalForm ());
	}
	
	@Test
	public void
	testCreateFromBIC()
	{
		ISO9362 bic = ISO9362.valueOf ( "DEUTDEFFXXX");
		// Deutsche Bank Frankfurt (DEFFXXX) has BLZ=50070010
		IBAN deIban = IBAN.create ( bic.getCountryCode (), "50070010",  "", "0532013000");
		Assert.assertEquals ( "DE04500700100532013000", deIban.externalForm ());
	}
	
	@Test
	public void
	testObfusticatedToString()
	{
		// FR:BANK=n5;BRANCH=n5;ACC=c13
		String testIban = "FR14 2004 1010 0505 0001 3M02 606";
		IBAN frIBAN = IBAN.parse ( testIban);
		Assert.assertFalse ( testIban.equals ( frIBAN.toString ()));
	}
	
	@Test
	public void
	testGuessScheme()
	{
		Assert.assertEquals ( "RS35260005601001611379", IBAN.parse("RS35 2600 0560 1001 6113 79").externalForm ());
		Assert.assertEquals ( "MC9320052222100112233M44555", IBAN.parse("MC93 2005 2222 1001 1223 3M44 555").externalForm());
		Assert.assertEquals ( "LU280019400644750000", IBAN.parse("LU28.0019.4006.4475.0000").externalForm());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void
	testCountryOnly()
	{
		IBAN.parse("IE");
	}
	
	@Test(expected=InvalidIBANException.class)
	public void
	testCountryPlusBankOnly()
	{
		IBAN.parse("IE99BOFI");
	}
	
	@Test
	public void
	testSortList()
	{
		List<IBAN> list = new ArrayList<>();
		for ( String number : TestNumbers)
		{
			IBAN iban = IBAN.parse(number);  
			list.add(iban);
		}
		Collections.sort(list);
		IBAN[] array = list.toArray(new IBAN[0]);
		Assert.assertEquals("CH9300762011623852957",array[0].externalForm());
		Assert.assertEquals("SA0380000000608010167519",array[array.length-1].externalForm());
	}
	
	@Test
	public void
	testAssemblerIBANIE()
	{
		IBAN irishNumber = IBAN.create ( COUNTRY_IE, "BOFI", "901490", "12345678");
		Assert.assertEquals ( "IE86BOFI90149012345678", irishNumber.externalForm());
	}
	
	@Test
	public void
	testAssemblerIBANGB()
	{
		IBAN gbNumber = IBAN.create ( ISO3166.valueOf ( "GB"), "WEST", "123456", "98765432");
		Assert.assertEquals ( "GB82WEST12345698765432", gbNumber.externalForm());
	}
	
	@Test
	public void
	testAssemblerIBAN()
	{
		IBAN malteseNumber = IBAN.create ( ISO3166.valueOf ( "MT"), "MALT", "01100", "0012345MTLCAST001S");
		Assert.assertEquals ( "MT84MALT011000012345MTLCAST001S", malteseNumber.externalForm());
	}
	
	// Global Bank of Commerce
	@Test
	public void
	testAssemblerGBCIBAN()
	{
		IBAN gbcNumber = IBAN.create ( ISO3166.valueOf ( "AG"), "GBCL", "1268", "00000009539");
		Assert.assertEquals ( "AG49GBCL126800000009539", gbcNumber.externalForm());
	}
	
	@Test
	public void
	testGuessSchemeAL()
	{
		IBAN alNumber = IBAN.parse("AL47 2121 1009 0000 0002 3569 8741");
		Assert.assertEquals ( ISO3166.valueOf("AL"), alNumber.getCountryCode ());
	}
	
	@Test
	public void
	testCompare()
	{
		IBAN number1 = IBAN.parse("AZ21.NABZ.0000.0000.1370.1000.1944");
		IBAN number2 = IBAN.parse("AZ21NABZ00000000137010001944");
		Assert.assertEquals(number1, number2);
	}
	
	@Test
	public void
	testParts()
	{
		IBAN number1 = IBAN.parse("NO93 8601 1117 947");
		Assert.assertEquals ( ISO3166.valueOf ( "NO"), number1.getCountryCode());
		Assert.assertEquals ( "8601", number1.getBankCode());
		Assert.assertEquals ( "1117947", number1.getAccountNumber());
	}
	
	@Test
	public void
	testGetBBAN_NL()
	{
		IBAN number1 = IBAN.parse("NL39 RABO 0300 0652 64");

		Assert.assertEquals ( "NL39RABO0300065264", number1.externalForm());
		BBAN bban = number1.getBBAN();
		Assert.assertEquals ( "RABO0300065264", bban.externalForm ());
		NL_BBAN nlbban = NL_BBAN.create ( number1.getAccountNumber ());
		Assert.assertEquals ( "0300065264", nlbban.externalForm ());
	}

	
	@Test(expected=InvalidIBANException.class)
	public void
	testUnknownScheme()
	{
		try
		{
			IBAN.parse("AE07 0331 2345 6789 0123 456");
		}
		catch ( InvalidIBANException e)
		{
			Assert.assertEquals ( ISO3166.valueOf ( "AE"), e.getCountryCode ());
			Assert.assertEquals ( "no scheme defined for country", e.getReason());
			throw e;
		}
	}
	
}
