package org.seefin.nygaard.model.identifiers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class TestMSISDNSchemes {
    private static final String[] testNumbersEG =
            {
                    "+20 10  123 3576", "+20 100 342 5643", // Vodafone
                    "+20 11  456 9425", "+20 111 652 8763", // Etisalat
                    "+20 12  789 7842", "+20 122 645 8853", // Mobinil
                    "+20 14  167 9633", "+20 114 853 2587", // Etisalat
                    "+20 150 352 0353", "+20 120 854 4632", // Mobinil
                    "+20 151 864 2114", "+20 101 582 5464", // Vodafone
                    "+20 152 913 5653", "+20 112 112 9465", // Etisalat
                    "+20 16  102 0053", "+20 106 970 3246", // Vodafone
                    "+20 17  458 4524", "+20 127 572 3323", // Mobinil
                    "+20 18  943 7685", "+20 128 982 9875", // Mobinil
                    "+20 19  636 6377", "+20 109 776 4587"  // Vodafone
            };

    private static final String[] testMsisdns =
            {"20 122 123 7543",
                    "20 114 123 7543",
                    "20 106 123 7543",
                    "20 127 123 7543",
                    "20 128 123 7543",
                    "20 109 123 7543"};

    @Value("${msisdn.scheme.specification}")
    private File testSchemaFile;

    //@Before
    public void
    setSchema()
            throws IOException {
        MSISDNScheme.loadSchemeFromFile(testSchemaFile);
    }

    @Test
    public void
    testGuessSchemeIE() {
        MSISDN number = MSISDN.parse("+353-86-3578380");
        Assert.assertEquals(353, number.getCC());
    }

    @Test
    public void
    testGuessScheme() {
        Assert.assertEquals("+201500000001", MSISDN.valueOf(201500000001L).externalForm());
        Assert.assertEquals("+44865249864", MSISDN.parse("+44.865.249.864").externalForm());
        MSISDN nb = MSISDN.parse("+234901220887");
        Assert.assertEquals("+234901220887", nb.externalForm());
    }

    @Test
    public void
    testEGNumbers() {
        for (String number : testNumbersEG) {
            MSISDN msisdn = MSISDN.parse(number);
            Assert.assertEquals(20, msisdn.getCC());
        }
    }

    @Test
    public void
    testEGNumbers2() {
        for (String number : testMsisdns) {
            MSISDN msisdn = MSISDN.parse(number);
            Assert.assertEquals(20, msisdn.getCC());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void
    testCountryOnly() {
        MSISDN.parse("+44");
    }

    @Test(expected = IllegalArgumentException.class)
    public void
    testCountryPlusNDCOnly() {
        MSISDN.parse("+35396");
    }

    @Test
    public void
    testSortList() {
        List<MSISDN> list = new ArrayList<>();
        for (String number : testNumbersEG) {
            MSISDN msisdn = MSISDN.parse(number);
            list.add(msisdn);
        }
        Collections.sort(list);
        MSISDN[] array = list.toArray(new MSISDN[0]);
        Assert.assertEquals("+20101233576", array[0].externalForm());
        Assert.assertEquals("+201529135653", array[array.length - 1].externalForm());
    }

    @Test
    public void
    testGuessSchemeUS() {
        MSISDN usNumber = MSISDN.parse("1 855 784-9261");
        Assert.assertEquals("+18557849261", usNumber.externalForm());
        Assert.assertEquals(1, usNumber.getCC());
        Assert.assertEquals(855, usNumber.getNDC());
        Assert.assertEquals(7849261, usNumber.getSN());
    }

    @Test
    public void
    testCompare() {
        MSISDN number1 = MSISDN.parse("+353-86-3578380");
        MSISDN number2 = MSISDN.valueOf(353863578380L);
        Assert.assertEquals(number1, number2);
    }

    @Test
    public void
    testParts() {
        MSISDN number1 = MSISDN.parse("+353-86-3578380");
        System.out.println("+[" + number1.getCC() + "]["
                + number1.getNDC() + "][" + number1.getSN() + "]");

    }

    @Test
    public void
    test2Integer() {
        MSISDN number1 = MSISDN.parse("+353-86-3578380");
        System.out.println(number1 + " = " + number1.longValue());
        MSISDN number2 = new MSISDN(353863578380L);
        System.out.println("number2=" + number2);
    }

    @Test
    public void
    testEgyptianNDC() {
        /*
         * Vodafone
         * 	Existing Number		New Numbering Plan
         * 	(0)10   XXX XXXX	(0)10 0  XXX XXXX
         * 	(0)16   XXX XXXX	(0)10 6  XXX XXXX
         * 	(0)19   XXX XXXX	(0)10 9  XXX XXXX
         * 	(0)151  XXX XXXX	(0)10 1  XXX XXXX
         * Old range        New range         Service
         * ---------        ---------         -------
         * +20 10  xxx xxxx   +20 100 xxx xxxx   Vodafone
         * +20 11  xxx xxxx   +20 111 xxx xxxx   Etisalat
         * +20 12  xxx xxxx   +20 122 xxx xxxx   Mobinil
         * +20 14  xxx xxxx   +20 114 xxx xxxx   Etisalat
         * +20 150 xxx xxxx   +20 120 xxx xxxx   Mobinil
         * +20 151 xxx xxxx   +20 101 xxx xxxx   Vodafone
         * +20 152 xxx xxxx   +20 112 xxx xxxx   Etisalat
         * +20 16  xxx xxxx   +20 106 xxx xxxx   Vodafone
         * +20 17  xxx xxxx   +20 127 xxx xxxx   Mobinil
         * +20 18  xxx xxxx   +20 128 xxx xxxx   Mobinil
         * +20 19  xxx xxxx   +20 109 xxx xxxx   Vodafone
         *
         * EG.Vodafone.9=2,2,7;CC=20;NDC=10,11,12,14,16,17,18,19
         * EG.Vodafone.10=2,3,7;CC=20;NDC=150,151,152,100,106,109,101
         * EG.Etisalat.9=2,2,7;CC=20;NDC=11,14
         * EG.Etisalat.10=2,3,7;CC=20;NDC=152
         */
        MSISDN number1 = MSISDN.parse("+20-10-1234567");
        MSISDN number2 = MSISDN.parse("+20-151-1234567");
        System.out.println("EG09=" + number1);
        System.out.println("EG10=" + number2);
    }

    @Test
    public void
    testEgyptianNDC2() {
        MSISDN number1 = MSISDN.parse("+20-151-0101223 ");
        MSISDN number2 = MSISDN.parse("+20-101-0101223");
        System.out.println("EG09=" + number1);
        System.out.println("EG10=" + number2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void
    testBadEgyptian() {
        MSISDN number1 = MSISDN.parse("+20-15-1234567");
        System.out.println("EG=" + number1);
    }

    @Test
    public void
    testNewVodafoneNDCs() {
        MSISDN number1 = MSISDN.parse("+20 100 123 4567");
        Assert.assertSame(100, number1.getNDC());
        MSISDN number2 = MSISDN.parse("+20 101 123 4567");
        Assert.assertSame(101, number2.getNDC());
        MSISDN number3 = MSISDN.parse("+20 106 123 4567");
        Assert.assertSame(106, number3.getNDC());
        MSISDN number4 = MSISDN.parse("+20 109 123 4567");
        Assert.assertSame(109, number4.getNDC());
    }

    @Test
    public void
    testAntigua() {
        MSISDN antigua = MSISDN.parse("+2687239010");
        System.out.println(antigua);
        Assert.assertEquals("+2687239010", antigua.externalForm());
    }

    @Test
    public void
    testHierarchy() {
        // test Greece, Holland, Ireland
        MSISDN dutch = MSISDN.parse("31628000000");
        System.out.println(dutch);
        Assert.assertEquals("+31628000000", dutch.externalForm());

        MSISDN greek = MSISDN.parse("+30-22-323232");
        System.out.println(greek);
        Assert.assertEquals("+3022323232", greek.externalForm());

        MSISDN irish = MSISDN.parse("00353863578380");
        System.out.println(irish);
        Assert.assertEquals("+353863578380", irish.externalForm());

    }

    @Test(expected = IllegalArgumentException.class)
    public void
    testUnknownScheme() {
        MSISDN number1 = MSISDN.parse("380561234567");
        Assert.assertNull(number1);
    }

}
