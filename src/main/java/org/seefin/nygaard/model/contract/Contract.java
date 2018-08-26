package org.seefin.nygaard.model.contract;

import org.seefin.nygaard.model.financial.Account;
import org.seefin.nygaard.model.parties.Party;
import org.seefin.nygaard.model.services.Service;


/**
 * Abstract notion of a contractual relationship between a supplier and a customer,
 * relating to a specific service offering
 *
 * @author phillipsr
 */
public abstract class Contract {
    private final Party customer;
    private final Service service;
    private Account account;

    public Contract(Service service, Party customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.customer = customer;
        this.service = service;
        account = createAccount(customer);
    }

    abstract protected Account createAccount(Party customer);

    public Party getCustomer() {
        return customer;
    }

    public Service getService() {
        return service;
    }

    /**
     * @return
     */
    public Account getAccount() {
        return account;
    }
}
