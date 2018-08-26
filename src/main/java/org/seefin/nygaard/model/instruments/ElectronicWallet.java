package org.seefin.nygaard.model.instruments;

import java.util.Currency;

import org.seefin.nygaard.model.financial.Account;
import org.seefin.nygaard.model.identifiers.EWalletAccountNumber;
import org.seefin.nygaard.model.identifiers.MSISDN;
import org.seefin.nygaard.model.parties.Organization;
import org.seefin.nygaard.model.parties.Party;


/**
 * Specialization of account representing an electronic wallet
 *
 * @author phillipsr
 */
public class ElectronicWallet
        extends Account {
    /**
     * Create a new electronic wallet account instance
     *
     * @param owner          of the new account
     * @param provider       of the account service
     * @param walletCurrency of the account balance, etc.
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public ElectronicWallet(Party owner, Organization provider, Currency walletCurrency) {
        super(owner, provider, walletCurrency, new EWalletAccountNumber((MSISDN) owner.getId()));
    }


}
