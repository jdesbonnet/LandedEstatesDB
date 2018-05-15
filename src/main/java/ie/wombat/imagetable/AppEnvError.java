/*
 * Created on May 14, 2005
 * 
 * (c) 2005-2008 Joe Desbonnet
 */
package ie.wombat.imagetable;

/**
 * Unchecked exception thrown when an unrecoverable situation is encoutered.
 * 
 * @author joe
 *
 */
public class AppEnvError extends Error {
	
	public AppEnvError (String s) {
		super(s);
	}
	public AppEnvError (Throwable e) {
		super(e);
	}

}
