package org.seefin.nygaard.model.identifiers;

import java.io.Serializable;

/**
 * Abstract notion of an identity, such as a name, phone or code number
 *
 * @author phillipsr
 */
public interface Identity
        extends Serializable {
    /**
     * @return Answers with a string form of this identity, generally in the format
     * commonly used to exchange or display the specific identity type<p/>
     * Due to the sensitive nature of identity information, <code>toString()</code>
     * will often be overridden to return an obfusticated version of the
     * identity, so this method is used to get the full version:<br/>
     * so <u>do not use this in log messages</u>
     * (instead, rely on slf4j's implicit toString() invocation)
     */
    public String externalForm();
}
