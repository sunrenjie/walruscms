package lt.walrus.utils;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class WalrusSecurity {

	public static boolean loggedOnUserHasAdminRole() {
		return hasRole("ROLE_ADMIN");
	}
	
	public static boolean hasRole(String role) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (null != auth && auth.isAuthenticated()) {
			Collection<GrantedAuthority> authz = auth.getAuthorities();
			if (null != authz) {
				for (GrantedAuthority authority : authz) {
					if (role.equals(authority.getAuthority())) {
						return true;
					}

				}
			}
		}
		
		return false;
	}
}
