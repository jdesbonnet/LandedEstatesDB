package ie.wombat.landedestates.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Can only be accessed from localhost
 * 
 * @author Joe Desbonnet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalhostOnly {

}
