package org.seefin.nygaard.model.requests;

import org.seefin.nygaard.model.channels.Channel;
import org.seefin.nygaard.model.financial.FinancialTransaction;


/**
 * This request bean represents the normalized concept of a credit transfer request
 *
 * @author phillipsr
 */
public final class TransferRequest
        extends Command {
    private FinancialTransaction transaction;
    private String comment;

    /**
     * Create a new credit transfer request
     *
     * @param requestor   the party making the request (customer, or an agent, teller, etc.)
     * @param transaction to be transferred
     * @param comment     text for transfer statement
     */
    public TransferRequest(
            Channel channel, FinancialTransaction transaction, String comment) {
        super(channel);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction may not be null");
        }
        if (comment == null) {
            throw new IllegalArgumentException("Comment may not be null");
        }
        this.transaction = transaction;
        this.comment = comment;
    }

    public FinancialTransaction getTransaction() {
        return transaction;
    }

    public String getComment() {
        return comment;
    }


    @Override
    public String
    toString() {
        return this.getClass().getSimpleName()
                + "{Channel: " + getChannel() + ", Transaction: " + transaction
                + ", Timestamp: " + getTimestamp()
                + ", Comment: " + comment + "}";
    }

}
