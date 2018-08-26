package org.seefin.nygaard.model.codes;

import java.io.Serializable;

/**
 * @author phillipsr
 */
public class ResponseMessage
        implements Serializable {
    private final boolean success;
    private final StatusCode code;
    private final String text;

    public ResponseMessage(boolean success, StatusCode code, String text) {
        this.success = success;
        this.code = code;
        this.text = text;
    }

    public boolean getSuccess() {
        return success;
    }

    public StatusCode getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    @Override
    public String
    toString() {
        return "ResponseMessage{success=" + success +
                ", code=" + code +
                ", text=\"" + text + "\"}";
    }

}
