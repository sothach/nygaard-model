/**
 *
 */
package org.seefin.nygaard.model.identifiers;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;

/**
 * @author phillipsr
 */
public class TestUniqueIdentity {
    private static final String ID_STRING = "F47AC10b-58cc-4372-a567-0e02b2c3d479";
    private static final String BAD_ID = "f47ac10b-58cc-4372-a567-0e02b2c3d47";

    @Ignore
    @Test
    public void
    testCreateTime() {
        long start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < 100000; i++) {
            GUID id = GUID.createUniqueId();
            id.toString();
        }
        long finish = Calendar.getInstance().getTimeInMillis();
        double time = (finish - start) / 100000.0;
        System.out.println("per-iteration " + time + "s");
    }

    @Test
    public void
    testNewIdLength() {
        GUID id = GUID.createUniqueId();
        System.out.println("id=" + id);
        Assert.assertEquals(32, id.externalForm().length());
    }

    @Test
    public void
    testParse() {
        GUID id = GUID.parse(ID_STRING);
        System.out.println("id=" + id);
        Assert.assertEquals(ID_STRING.replaceAll("-", ""), id.externalForm());
    }

    @Test
    public void
    testIfValid() {
        Assert.assertTrue(GUID.isValid(ID_STRING));
    }

    @Test
    public void
    testNotValid() {
        Assert.assertFalse(GUID.isValid(BAD_ID));
    }

    @Test
    public void
    testCompare() {
        GUID id1 = GUID.parse(ID_STRING);
        GUID id2 = GUID.parse(ID_STRING);

        Assert.assertTrue(id1.compareTo(id2) == 0);
    }

    @Test
    public void
    testEquals() {
        GUID id1 = GUID.parse(ID_STRING);
        GUID id2 = GUID.parse(ID_STRING);

        Assert.assertEquals(id1, id2);
    }

    @Test
    public void
    testNotEquals() {
        GUID id1 = GUID.parse(ID_STRING);
        GUID id2 = GUID.createUniqueId();

        Assert.assertFalse(id1.equals(id2));
    }
}
