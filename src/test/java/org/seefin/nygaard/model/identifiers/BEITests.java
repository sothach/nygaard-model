package org.seefin.nygaard.model.identifiers;

import org.junit.Assert;
import org.junit.Test;
import org.seefin.nygaard.model.locations.ISO3166;


/**
 * @author phillipsr
 */
public class BEITests {
    private static final ISO3166 DE_CODE = ISO3166.valueOf("de");
    private static final ISO3166 IT_CODE = ISO3166.valueOf("it");
    private static final ISO3166 CN_CODE = ISO3166.valueOf("cn");
    private static final ISO3166 NE_CODE = ISO3166.valueOf("ne");
    private static final ISO3166 US_CODE = ISO3166.valueOf("us");

    @Test
    public void
    testGoodBIC8() {
        ISO9362 code = ISO9362.valueOf("DEUTDEFF");
        Assert.assertEquals("DEUT", code.getInstitutionCode());
        Assert.assertEquals(DE_CODE, code.getCountryCode());
        Assert.assertEquals("FF", code.getLocationCode());
        Assert.assertEquals("", code.getBranchCode());
    }

    @Test
    public void
    testGoodBIC8Construction() {
        ISO9362 code = new ISO9362("ECOC", NE_CODE, "NI");
        Assert.assertEquals("ECOCNENI", code.externalForm());
    }

    @Test
    public void
    testGoodBIC11Construction() {
        ISO9362 code = new ISO9362("UNCR", IT_CODE, "2B", "912");
        Assert.assertEquals("UNCRIT2B912", code.externalForm());
    }

    @Test
    public void
    testGoodBIC11() {
        ISO9362 code = ISO9362.valueOf("DSBACNBXSHA");
        Assert.assertEquals("DSBA", code.getInstitutionCode());
        Assert.assertEquals(CN_CODE, code.getCountryCode());
        Assert.assertEquals("BX", code.getLocationCode());
        Assert.assertEquals("SHA", code.getBranchCode());
    }

    @Test
    public void
    testGoodBIC11Parsing() {
        ISO9362 code = ISO9362.valueOf("GUBCUS6L");
        Assert.assertEquals("GUBC", code.getInstitutionCode());
        Assert.assertEquals(US_CODE, code.getCountryCode());
        Assert.assertEquals("6L", code.getLocationCode());
        Assert.assertTrue(code.getBranchCode().isEmpty());
    }

    @Test
    public void
    compareEquals() {
        Assert.assertEquals(new ISO9362("UNCR", IT_CODE, "2B", "912"), ISO9362.valueOf("UNCRIT2B912"));
    }


}
