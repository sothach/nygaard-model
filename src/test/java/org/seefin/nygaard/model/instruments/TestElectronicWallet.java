package org.seefin.nygaard.model.instruments;

import org.junit.Assert;
import org.junit.Test;
import org.seefin.nygaard.model.identifiers.DUNS;
import org.seefin.nygaard.model.identifiers.MSISDN;
import org.seefin.nygaard.model.identifiers.PersonalName;
import org.seefin.nygaard.model.parties.Individual;
import org.seefin.nygaard.model.parties.Organization;
import org.seefin.nygaard.model.parties.Party;

import java.util.Currency;


public class TestElectronicWallet {
    private static final MSISDN TEST_MSISDN = MSISDN.parse("+2687239010");

    @Test
    public void
    testElectronicWalletCreation() {
        PersonalName name = new PersonalName(new String[]{"Joe", "Customer"});
        Party party = new Individual(name, TEST_MSISDN);
        Organization provider
                = new Organization(DUNS.parse("039189524"), "Cyprus MFS, Inc.");

        ElectronicWallet wallet = new ElectronicWallet(party, provider, Currency.getInstance("EUR"));
        System.out.println(wallet.toString());
        Assert.assertNotNull(wallet);
        Assert.assertEquals("+2687239010", wallet.getHolder().getId().externalForm());
        Assert.assertEquals("039189524", wallet.getProvider().getId().externalForm());
    }
}
