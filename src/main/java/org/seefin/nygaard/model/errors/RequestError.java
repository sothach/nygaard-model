package org.seefin.nygaard.model.errors;

import java.io.Serializable;
import java.util.Map;

/**
 * Models the error information associated with a failed request
 * 
 * @author phillipsr
 *
 */
public class RequestError
	implements Serializable
{
	private Map<?,?> request;
	private Exception exception;

	/**
	 * @param request
	 * @param exception
	 */
	public RequestError ( Map<?,?> request, Exception exception)
	{
		this.request = request;
		this.exception = exception;
	}

	public Map<?,?> getRequest () { return request; }
	public Exception getExecption () { return exception; }

	@Override
	public String
	toString()
	{
		return "request: " + request + " exception: " + exception;
	}
}
