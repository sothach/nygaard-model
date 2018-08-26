package org.seefin.nygaard.model.identifiers;

import org.junit.Test;
import org.seefin.nygaard.model.locations.ISO3166;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class TestIBANSchemes {
    private static final String[] TestNumbers = {"IE86 BOFI 9014 9012 3456 78", // Ireland
            "GR16 0110 1250 0000 0001 2300 695", // Greece
            "GB29 NWBK 6016 1331 9268 19", // United Kingdom
            "SA03 8000 0000 6080 1016 7519", // Saudi Arabia
            "CH93 0076 2011 6238 5295 7", // Switzerland
            "IL62 0108 0000 0009 9999 999" // Israel	
    };
    private static final ISO3166 COUNTRY_IE = ISO3166.valueOf("IE");

    /*
     * IE:BANK=c4;BRANCH=n6;ACC=n8
     */
    @Test
    public void testGuessSchemeIE() {
        final IBAN number = IBAN.parse("IE 64 IRCE 920501 12345678");
        assertThat(number.externalForm(), is("IE64IRCE92050112345678"));
        assertThat(number.getCountryCode(), is(COUNTRY_IE));
        assertThat(number.getBBAN(), is(new BBAN("IRCE92050112345678")));
        assertThat(number.getBankCode(), is("IRCE"));
        assertThat(number.getBranchCode(), is("920501"));
        assertThat(number.getAccountNumber(), is("12345678"));
    }

    @Test(expected = InvalidIBANException.class)
    public void testInvalidIE() {
        try {
            IBAN.parse("IE64IRCE9205011234567");
        } catch (InvalidIBANException e) {
            assertThat(e.getCountryCode(), is(COUNTRY_IE));
            assertThat(e.getReason(), is("IBAN string must be 22 characters in length"));
            throw e;
        }
    }

    @Test(expected = InvalidIBANException.class)
    public void testInvalidMod97() {
        try {
            IBAN.parse("IE64IRCE92050112345679");
        } catch (InvalidIBANException e) {
            assertThat(e.getCountryCode(), is(COUNTRY_IE));
            assertThat(e.getReason(), is("Mod97 checksum failed"));
            throw e;
        }
    }

    @Test
    public void testUS() {
        // US IBAN doesn't exists, but could be constructed from the ABA MICR and the account number:
        // The MICR number is of the form XXXXYYYYC
        // where XXXX is Federal Reserve Routing Symbol, YYYY is ABA Institution Identifier, and C is the Check Digit
        // Routing # Checking Acct. #
        // 212456251 100003456789
        IBAN usIban = IBANScheme.create(ISO3166.valueOf("US"), "212456251", "", "100003456789");
        assertThat(usIban.externalForm(), is("US97212456251100003456789"));
    }

    @Test
    public void testCreateFromBIC() {
        final ISO9362 bic = ISO9362.valueOf("DEUTDEFFXXX");
        // Deutsche Bank Frankfurt (DEFFXXX) has BLZ=50070010
        final IBAN deIban = IBAN.create(bic.getCountryCode(), "50070010", "", "0532013000");
        assertThat(deIban.externalForm(), is("DE04500700100532013000"));
    }

    @Test
    public void testObfusticatedToString() {
        // FR:BANK=n5;BRANCH=n5;ACC=c13
        final String testIban = "FR14 2004 1010 0505 0001 3M02 606";
        final IBAN frIBAN = IBAN.parse(testIban);
        assertThat(frIBAN.toString(), is("FR142********************06"));
    }

    @Test
    public void testGuessScheme() {
        assertThat(IBAN.parse("RS35 2600 0560 1001 6113 79").externalForm(), is("RS35260005601001611379"));
        assertThat(IBAN.parse("MC93 2005 2222 1001 1223 3M44 555").externalForm(), is("MC9320052222100112233M44555"));
        assertThat(IBAN.parse("LU28.0019.4006.4475.0000").externalForm(), is("LU280019400644750000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountryOnly() {
        IBAN.parse("IE");
    }

    @Test(expected = InvalidIBANException.class)
    public void testCountryPlusBankOnly() {
        IBAN.parse("IE99BOFI");
    }

    @Test
    public void
    testSortList() {
        final List<IBAN> list = new ArrayList<>();
        for (String number : TestNumbers) {
            IBAN iban = IBAN.parse(number);
            list.add(iban);
        }
        Collections.sort(list);
        final IBAN[] array = list.toArray(new IBAN[0]);
        assertThat(array[0].externalForm(), is("CH9300762011623852957"));
        assertThat(array[array.length - 1].externalForm(), is("SA0380000000608010167519"));
    }

    @Test
    public void testAssemblerIBANIE() {
        final IBAN irishNumber = IBAN.create(COUNTRY_IE, "BOFI", "901490", "12345678");
        assertThat(irishNumber.externalForm(), is("IE86BOFI90149012345678"));
    }

    @Test
    public void testAssemblerIBANGB() {
        final IBAN gbNumber = IBAN.create(ISO3166.valueOf("GB"), "WEST", "123456", "98765432");
        assertThat(gbNumber.externalForm(), is("GB82WEST12345698765432"));
    }

    @Test
    public void testAssemblerIBAN() {
        final IBAN malteseNumber = IBAN.create(ISO3166.valueOf("MT"), "MALT", "01100", "0012345MTLCAST001S");
        assertThat(malteseNumber.externalForm(), is("MT84MALT011000012345MTLCAST001S"));
    }

    // Global Bank of Commerce
    @Test
    public void testAssemblerGBCIBAN() {
        final IBAN gbcNumber = IBAN.create(ISO3166.valueOf("AG"), "GBCL", "1268", "00000009539");
        assertThat(gbcNumber.externalForm(), is("AG49GBCL126800000009539"));
    }

    @Test
    public void testGuessSchemeAL() {
        IBAN alNumber = IBAN.parse("AL47 2121 1009 0000 0002 3569 8741");
        assertThat(alNumber.getCountryCode(), is(ISO3166.valueOf("AL")));
    }

    @Test
    public void testCompare() {
        final IBAN number1 = IBAN.parse("AZ21.NABZ.0000.0000.1370.1000.1944");
        final IBAN number2 = IBAN.parse("AZ21NABZ00000000137010001944");
        assertThat(number1, is(number2));
        assertThat(number1.hashCode(), is(number2.hashCode()));
    }

    @Test
    public void testParts() {
        final IBAN number1 = IBAN.parse("NO93 8601 1117 947");
        assertThat(number1.getCountryCode(), is(ISO3166.valueOf("NO")));
        assertThat(number1.getBankCode(), is("8601"));
        assertThat(number1.getAccountNumber(), is("1117947"));
    }

    @Test
    public void testGetBBAN_NL() {
        final IBAN number1 = IBAN.parse("NL39 RABO 0300 0652 64");

        assertThat(number1.externalForm(), is("NL39RABO0300065264"));
        final BBAN bban = number1.getBBAN();
        assertThat(bban.externalForm(), is("RABO0300065264"));
        final NL_BBAN nlbban = NL_BBAN.create(number1.getAccountNumber());
        assertThat(nlbban.externalForm(), is("0300065264"));
    }


    @Test(expected = InvalidIBANException.class)
    public void testUnknownScheme() {
        try {
            IBAN.parse("AE07 0331 2345 6789 0123 456");
        } catch (InvalidIBANException e) {
            assertThat(e.getCountryCode(), is(ISO3166.valueOf("AE")));
            assertThat(e.getReason(), is("no scheme defined for country"));
            throw e;
        }
    }

}
