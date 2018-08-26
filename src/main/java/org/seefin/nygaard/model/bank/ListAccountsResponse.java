package org.seefin.nygaard.model.bank;

import java.util.List;

import org.seefin.nygaard.model.collections.WormList;


/**
 * @author phillipsr
 */
public class ListAccountsResponse {
    private final WormList<AccountDetails> accounts;

    public ListAccountsResponse(List<AccountDetails> accounts) {
        this.accounts = new WormList<>(accounts);
    }

    public WormList<AccountDetails> getAccounts() {
        return accounts;
    }
}
