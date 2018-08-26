package org.seefin.nygaard.model.identifiers;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author phillipsr
 */
public class TestISO7812 {
    private static final Long[] TestNumbers =
            {
                    378282246310005L,
                    371449635398431L,
                    378734493671000L,
                    5610591081018250L,
                    30569309025904L,
                    38520000023237L,
                    6011111111111117L,
                    6011000990139424L,
                    3530111333300000L,
                    3566002020360505L,
                    5555555555554444L,
                    5105105105105100L,
                    4111111111111111L,
                    4012888888881881L,
                    4222222222222L
            };

    @Test
    public void
    testNumberConversions() {
        for (Long l : TestNumbers) {
            ISO7812 number = ISO7812.valueOf(l);
            Assert.assertEquals(l, number.longValue());
        }
    }

    @Test
    public void
    testSortList() {
        List<ISO7812> list = new ArrayList<>();
        for (Long number : TestNumbers) {
            ISO7812 iso7812 = ISO7812.valueOf(number);
            list.add(iso7812);
        }
        Collections.sort(list);
        ISO7812[] array = list.toArray(new ISO7812[0]);
        Assert.assertEquals("4222222222222", array[0].externalForm());
        Assert.assertEquals("6011111111111117", array[array.length - 1].externalForm());
    }

    @Test(expected = InvalidISO7812Exception.class)
    public void
    testUnknownIssuer() {
        try {
            ISO7812.valueOf(5019717010103742L);
        } catch (InvalidISO7812Exception e) {
            Assert.assertEquals("No scheme registered", e.getReason());
            Assert.assertEquals("5019717", e.getErrorPart());
            throw e;
        }
    }

    @Test(expected = InvalidISO7812Exception.class)
    public void
    testInvalidChecksum() {
        try {
            ISO7812.valueOf(5610591081018251L);
        } catch (InvalidISO7812Exception e) {
            Assert.assertEquals("CardNumber checksum invalid", e.getReason());
            throw e;
        }
    }

    @Test(expected = InvalidISO7812Exception.class)
    public void
    testBadNumberlength() {
        try {
            ISO7812.valueOf(37144963539849L);
        } catch (InvalidISO7812Exception e) {
            Assert.assertEquals("CardNumber length must one of: [15]", e.getReason());
            throw e;
        }
    }

    @Test
    public void
    testObfustication() {
        ISO7812 number = ISO7812.parse("6011601160116611");

        Assert.assertFalse("6011601160116611".equals(number.toString()));
        Assert.assertEquals("6011601160116611", number.externalForm());

        Assert.assertEquals("Merchandizing and banking", number.getMajorIndustryName());
        Assert.assertEquals("Discover", number.getIssuer().getCommonName());
        Assert.assertTrue(16011661 == number.getAccountNumber());
        Assert.assertTrue(1 == number.longValue() % 10);
    }

    @Test
    public void
    testParts() {
        ISO7812 number = ISO7812.parse("4319408982824446");

        Assert.assertEquals("Banking and financial", number.getMajorIndustryName());
        Assert.assertEquals("VISA", number.getIssuer().getCommonName());
        Assert.assertTrue(98282444 == number.getAccountNumber());
        Assert.assertTrue(6 == number.longValue() % 10);
    }

    /**
     * This test confirms that, even if a scheme hasn't been registered (as the 50* BIN in this test),
     * a usable code is still created, only without issuer information; the Luhn check will have
     * checked that the number is otherwise well-formed
     */
    @Test
    public void
    testUnregisteredScheme() {
        ISO7812 number = new ISO7812(5019717010103742L); // constructor circumvents registered scheme check
        // check values that are available for all codes, registered or not:
        Assert.assertEquals("5019717010103742", number.externalForm());
        Assert.assertEquals("Banking and financial", number.getMajorIndustryName());
        Assert.assertTrue(5019717 == number.getIssuerIdentifier().intValue());
        Assert.assertTrue(1010374 == number.getAccountNumber());
        Assert.assertTrue(2 == number.longValue() % 10);

        // check that the issuer is identified by the BIN number extracted from the test number
        Assert.assertTrue(number.getIssuer().getId() instanceof BIN);
        Assert.assertTrue(5019717 == ((BIN) number.getIssuer().getId()).intValue());
        Assert.assertTrue(number.getIssuer().getCommonName().isEmpty());
    }

}
