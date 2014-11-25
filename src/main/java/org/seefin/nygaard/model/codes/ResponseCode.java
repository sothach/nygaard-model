package org.seefin.nygaard.model.codes;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Set of the standard response codes mapped for back-end services
 * 
 * @author phillipsr
 *
 */
public enum ResponseCode
{
	SUCCESS(0),
	SYSTEM_ERROR(1),
	COMMS_ERROR(2),
	SERVICE_UNAVAILABLE(3),
	REQUEST_FAILED(4),
	REQUEST_TIMEOUT(5),
	NOT_IMPLEMENTED(6),
	UNKNOWN_SERVICE(7),
	UNKNOWN_REQUEST(8),
	UNKNOWN_ACCOUNT(9),
	DATA_ACCESS_ERROR(10),
	
	INSUFFICENT_FUNDS(20),
	INVALID_PIN(21),
	CREDIT_LIMIT_EXCEEDED(22),
	INVALID_AMOUNT(23),
	INVALID_DATE(24),
	INVALID_CURRENCY(25),
	INVALID_ACCOUNT(26),
	INVALID_BANK(27),
	INVALID_CARD(28),
	INVALID_CHANNEL(29),
	INVALID_ACCOUNT_STATE(30),
	INVALID_REQUEST(31),
	INVALID_PROCESS_STATE(32),
	
	ACCOUNT_LIMIT_REACHED(40),
	TRANSACTION_LIMIT_REACHED(41),
	UNKNOWN_PARTY(42),
	
	UNAUTHORIZED(50),
	INVALID_PASSWORD(51),
	
	UNKNOWN_ERROR(999);
	
	private int code;
	private String description;
	private ResponseCode(int code,String description)
	{
		this.code = code;
		this.description = description;
	}
	
	private ResponseCode(int code)
	{
		this.code = code;
		this.description= "";
	}
	
	public int getCode() { return code; }
	
	public String getDescription() { return description; }
	
	/**
	 * @param text (i.e., from ws message) representation of transaction type 
	 * @return answers with instance corresponding to text label
	 * @post external form preserved: result.toString().equals(text)
	 */
	public static ResponseCode
	parse ( String text)
	{ 
		ResponseCode result = lookup.get(Integer.parseInt ( text.trim()));
		if ( result == null)
		{
			return null;
		}

	    return result;
	}
	

	public static ResponseCode
	valueOf ( int code)
	{ 
		ResponseCode result = lookup.get(code);
		if ( result == null)
		{
			throw new IllegalArgumentException ( 
					"No enum " + ResponseCode.class.getCanonicalName() + "." + code); 
		}
		assert result.getCode() == code : "external form preserved";
	    return result;
	}
	
    private static final Map<Integer,ResponseCode> lookup = new HashMap<>();
    static
    {
    	for(ResponseCode s : EnumSet.allOf(ResponseCode.class))
    	{
    		lookup.put ( s.getCode(), s);
    	}
    }
}
