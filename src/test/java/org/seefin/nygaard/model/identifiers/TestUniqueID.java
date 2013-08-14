/**
 * 
 */
package org.seefin.nygaard.model.identifiers;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.seefin.nygaard.model.identifiers.UniqueID;


/**
 * @author phillipsr
 *
 */
public class TestUniqueID
{
	private static final String ID_STRING = "1HGE-AW20-H1DK-Z5FW-11";
	private static final String BAD_ID = "1HGE-AW20-H1DK-Z5FW";

	@Ignore
	@Test
	public void
	testCreateTime()
	{
		Set<UniqueID> ids = new HashSet<>();
		long start = Calendar.getInstance ().getTimeInMillis ();
		double count = 1000000.0;
		for ( int i = 0; i < count; i++)
		{
			UniqueID id = UniqueID.createUnqiueId ();
			//System.out.println ( "[" + id + "]");
			Assert.assertFalse ( "ID clash [" + id + "] @ " + i, ids.contains ( id));
			ids.add ( id);
		}
		long finish = Calendar.getInstance ().getTimeInMillis ();
		double time = (finish-start)/count;
		System.out.println ( "per-iteration " + time + "s");
	}
	
	@Test
	public void
	testIdFromString()
	{
		UniqueID id = UniqueID.valueOf ( ID_STRING);
		Assert.assertEquals ( id.toString (), "1HGEAW20H1DKZ5FW11");
	}
	
	@Test
	public void
	testNewIdLength()
	{
		UniqueID id = UniqueID.createUnqiueId ();
		Assert.assertEquals ( 18, id.toString ().length ());
	}
	
	@Test
	public void
	testParse()
	{
		UniqueID id = UniqueID.fromString ( ID_STRING);
		Assert.assertEquals ( ID_STRING.replaceAll ( "[^A-Za-z0-9]", ""), id.toString ());
	}
	
	@Test
	public void
	testIfValid()
	{
		Assert.assertTrue ( UniqueID.isValid ( ID_STRING));
	}
	
	@Test
	public void
	testNotValid()
	{
		Assert.assertFalse ( UniqueID.isValid ( BAD_ID));
	}
	
	@Test
	public void
	testCompare()
	{
		UniqueID id1 = UniqueID.fromString ( ID_STRING);
		UniqueID id2 = UniqueID.fromString ( ID_STRING);

		Assert.assertTrue ( id1.compareTo ( id2) == 0);
	}
	
	@Test
	public void
	testEquals()
	{
		UniqueID id1 = UniqueID.fromString ( ID_STRING);
		UniqueID id2 = UniqueID.fromString ( ID_STRING);

		Assert.assertTrue ( id1.equals ( id2));
	}
	
	@Test
	public void
	testNotEquals()
	{
		UniqueID id1 = UniqueID.fromString ( ID_STRING);
		UniqueID id2 = UniqueID.createUnqiueId ();

		Assert.assertFalse ( id1.equals ( id2));
	}
	
	@Test
	public void
	testUniqueSequence()
	{
		Set<UniqueID> ids = new HashSet<>();
		for ( int i = 0; i < 100; i++)
		{
			UniqueID id = UniqueID.createUnqiueId ();
			Assert.assertFalse ( ids.contains ( id));
			ids.add ( id);
		}
		
		for ( UniqueID id : ids)
		{
			System.out.println ( "[" + id + "]");
		}
	}
}
