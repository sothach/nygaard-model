package org.seefin.nygaard.model.instruments;

import java.util.Currency;

import org.seefin.nygaard.model.financial.Account;
import org.seefin.nygaard.model.identifiers.AccountNumber;
import org.seefin.nygaard.model.parties.FinancialInstitution;
import org.seefin.nygaard.model.parties.Party;


/**
 * @author phillipsr
 */
public class BankAccount
        extends Account {

    public BankAccount(Party owner, FinancialInstitution provider, AccountNumber accountId, Currency accountCurrency) {
        super(owner, provider, accountCurrency, accountId);
    }


}
