package appeng.api.networking.security;

import appeng.api.config.SecurityPermissions;
import java.util.EnumSet;

/**
 * Used by vanilla Security Terminal to post biometric data into the security cache.
 */
public interface ISecurityRegister
{

	/**
	 * Submit Permissions into the register.
	 * 
	 * @param PlayerID
	 * @param permissions
	 */
	void addPlayer(int PlayerID, EnumSet<SecurityPermissions> permissions);

}
