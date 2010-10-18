package lt.walrus.utils;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;

public class WalrusSecurity {

	public static boolean loggedOnUserHasAdminRole() {
		return hasRole("ROLE_ADMIN");
	}
	
	public static boolean hasRole(String role) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (null != auth && auth.isAuthenticated()) {
			GrantedAuthority[] authz = auth.getAuthorities(); 
			if (null != authz) {
				for (int i = 0; i < authz.length; i++) {
					if (role.equals(authz[i].getAuthority())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
