package org.seefin.nygaard.model.channels;

import org.joda.time.DateTime;
import org.seefin.nygaard.model.identifiers.Identity;
import org.seefin.nygaard.model.identifiers.MSISDN;
import org.seefin.nygaard.model.identifiers.ReferenceCode;
import org.seefin.nygaard.model.parties.Subscriber;


/**
 * @author phillipsr
 */
public class SMSChannel
        extends Channel {
    public SMSChannel(Subscriber sender, Identity id) {
        super(sender, id);
    }

    /**
     * @param msisdn
     * @return a unique Id for the SMS request by concatenating
     * the current time-stamp with the sender's MSISDN
     */
    public static Identity
    createId(MSISDN msisdn) {
        DateTime timestamp = new DateTime();
        return new ReferenceCode(msisdn.longValue() + "" + timestamp.getMillis());
    }

}
