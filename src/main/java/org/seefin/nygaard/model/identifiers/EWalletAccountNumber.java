package org.seefin.nygaard.model.identifiers;

/**
 * An account number assigned to an electronic wallet
 * <p/>
 * TODO: it might be necessary to extend the number created here,
 * perhaps by a prefix, to enable the provider to be decoded...?
 *
 * @author phillipsr
 */
public class EWalletAccountNumber
        extends AccountNumber {
    private MSISDN msisdn;

    /**
     * Create the account number from the supplied MSISDN
     *
     * @param msisdn representing the e-wallet account
     */
    public EWalletAccountNumber(MSISDN msisdn) {
        this.msisdn = msisdn;
    }

    @Override
    public String externalForm() {
        return msisdn.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(msisdn.toString());
        return super.obsusticate(builder.reverse().toString(), 4, 2);
    }
}
