package ie.wombat.landedestates.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for API handler to indicate that a JSONP response is required (experimental).
 * See  {@link https://en.wikipedia.org/wiki/JSONP} 
 * 
 * @author Joe Desbonnet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONPResponse {

}
