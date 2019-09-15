package ie.wombat.landedestates.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for API handler to indicate that the HTTP GET method is allowed on it.
 * 
 * @author Joe Desbonnet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GetAllowed {

}
