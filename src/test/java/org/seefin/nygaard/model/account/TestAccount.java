package org.seefin.nygaard.model.account;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Assert;

import org.junit.Test;
import org.seefin.nygaard.model.financial.Account;
import org.seefin.nygaard.model.financial.MonetaryAmount;
import org.seefin.nygaard.model.identifiers.DUNS;
import org.seefin.nygaard.model.identifiers.ISO7812;
import org.seefin.nygaard.model.identifiers.MSISDN;
import org.seefin.nygaard.model.identifiers.PersonalName;
import org.seefin.nygaard.model.instruments.CreditCard;
import org.seefin.nygaard.model.instruments.ElectronicWallet;
import org.seefin.nygaard.model.parties.Individual;
import org.seefin.nygaard.model.parties.Organization;


/**
 * Contract tests for Account
 * 
 * Tests that specify the contract of account classes
 * @author phillipsr
 *
 */
public class TestAccount
{
	private static final Organization VENDOR = new Organization ( new DUNS ( 39189524), "Cyprus MFS");
	private static final MonetaryAmount BALANCE_AFTER 
		= MonetaryAmount.fromBigDecimal ( Currency.getInstance ( "EUR"), BigDecimal.TEN.add ( BigDecimal.TEN));
	private static final PersonalName TEST_CUSTOMER_NAME = new PersonalName ( new String[] { "Joe", "Blochs"});
	private static final MSISDN TEST_CUSTOMER_ID = MSISDN.parse ( "+353863567279");
	private static final Individual TEST_CUSTOMER = new Individual ( TEST_CUSTOMER_NAME, TEST_CUSTOMER_ID);
	protected static final Currency EURO_CURRENCY =  Currency.getInstance ( "EUR");	
	
	@Test
	public void
	testWalletCreation()
	{
		Account account = new ElectronicWallet ( TEST_CUSTOMER, VENDOR, EURO_CURRENCY);
	
		account.setBalance ( BALANCE_AFTER);
		
		Assert.assertEquals ( BALANCE_AFTER, account.getBalance());
	}
	
	@Test
	public void
	testCreditCardCreation()
	{
		Account account = new CreditCard ( TEST_CUSTOMER, EURO_CURRENCY, new ISO7812 ( 5105105105105100L));
	
		account.setBalance ( BALANCE_AFTER);
		
		Assert.assertEquals ( BALANCE_AFTER, account.getBalance());
	}	
}
