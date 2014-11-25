package org.seefin.nygaard.model.identifiers;

import org.junit.Assert;
import org.junit.Test;
import org.seefin.nygaard.model.identifiers.LuhnChecksum;

/**
 * @author phillipsr
 *
 */
public class LuhnChecker
{
	@Test
	public void
	testLuhnChecksumCalculation()
	{
		String candidate = "7992739871";
		int cd = LuhnChecksum.getCheckDigit ( candidate);
		Assert.assertEquals ( cd, 3);
	}
	
	@Test
	public void
	testLuhnChecksumValid()
	{
		String candidate = "79927398713";
		Assert.assertTrue ( LuhnChecksum.isValid ( candidate));
	}
	
	@Test
	public void
	testLuhnChecksumInvalid()
	{
		String candidate = "79927398712";
		Assert.assertFalse ( LuhnChecksum.isValid ( candidate));
	}
	
	@Test
	public void
	testLuhnChecksumCalculation2()
	{
		String candidate = "37828224631000";
		int cd = LuhnChecksum.getCheckDigit ( candidate);
		Assert.assertEquals ( cd, 5);
	}
	
	@Test
	public void
	testLuhnChecksumCalculation3()
	{
		String candidate = "431940898282444";
		int cd = LuhnChecksum.getCheckDigit ( candidate);
		Assert.assertEquals ( cd, 6);
	}	
	
	@Test
	public void
	testLuhnChecksumCalculation4()
	{
		String candidate = "633865026568976";
		int cd = LuhnChecksum.getCheckDigit ( candidate);
		Assert.assertEquals ( 2, cd);
		
		Assert.assertTrue ( LuhnChecksum.isValid ( "6338650265689762" ));
	}	
	
}
