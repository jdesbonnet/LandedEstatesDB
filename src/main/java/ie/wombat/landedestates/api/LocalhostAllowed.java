package ie.wombat.landedestates.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for API handler to indicate that authentication is not required if the request comes from localhost
 * 
 * @author Joe Desbonnet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalhostAllowed {

}
