package org.seefin.nygaard.model.identifiers;

import org.junit.Assert;

import org.junit.Test;
import org.seefin.nygaard.model.identifiers.MSISDN;
import org.seefin.nygaard.model.identifiers.MSISDNScheme;


public class NoSchemasFileTest
{
	@Test(expected=IllegalArgumentException.class)
	public void
	testNoSchemasAvailable()
	{
	    try
	    {
    		MSISDNScheme.loadScheme ( "");
    		MSISDN.valueOf ( 35315267251L);
	    }
	    catch ( IllegalArgumentException e)
	    {
	        Assert.assertTrue(e.getMessage().startsWith(MSISDNScheme.SCHEMA_LOCATION_INVALID));
	        throw e;
	    }
        finally
        {
            MSISDNScheme.resetScheme();
        }
	}
}
