package org.seefin.nygaard.model.codes;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author phillipsr
 */
public class TestResponseCodes {
    @Test
    public void
    testCreateCode() {
        StatusCode code = StatusCode.createCode(3, 2, 1, 44);

        Assert.assertEquals(30201044, code.intValue());
        Assert.assertEquals("302-01-044", code.toString());
        Assert.assertEquals(3, code.getZone());
        Assert.assertEquals(2, code.getModule());
        Assert.assertEquals(1, code.getComponent());
        Assert.assertEquals(44, code.getResponse());
    }

    @Test
    public void
    testCreateCodeFromInteger() {
        StatusCode code = StatusCode.createCode(30201999);

        Assert.assertEquals(30201999, code.intValue());
        Assert.assertEquals("302-01-999", code.toString());
        Assert.assertEquals(3, code.getZone());
        Assert.assertEquals(2, code.getModule());
        Assert.assertEquals(1, code.getComponent());
        Assert.assertEquals(999, code.getResponse());
    }

}
