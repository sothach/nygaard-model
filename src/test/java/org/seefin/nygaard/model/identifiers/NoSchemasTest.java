package org.seefin.nygaard.model.identifiers;

import org.junit.Assert;

import org.junit.Test;
import org.seefin.nygaard.model.identifiers.MSISDN;
import org.seefin.nygaard.model.identifiers.MSISDNScheme;


public class NoSchemasTest
{
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void
	testUnrecognizedSchema()
	{
	    try
	    {
    		@SuppressWarnings("unused")
    		MSISDN testNumber = MSISDN.parse ( "+35315267251");
	    }
	    catch ( IllegalArgumentException e)
	    {
	        Assert.assertTrue(e.getMessage().startsWith(MSISDNScheme.UNRECOGNIZED_SCHEME));
	        throw e;
	    }
	}
}
