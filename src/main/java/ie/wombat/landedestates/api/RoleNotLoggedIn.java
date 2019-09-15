package ie.wombat.landedestates.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Special role indicating that a valid login session is not required.
 * 
 * @author Joe Desbonnet
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleNotLoggedIn {

}
