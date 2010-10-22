package lt.walrus.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Authentication wrapper that makes it easy to add additional roles to
 * authentication
 */
public class AuthenticationWrapper implements Authentication {
	private static final long serialVersionUID = 3404314781040339823L;
	private Authentication original;
	private Collection<GrantedAuthority> extraRoles;

	public AuthenticationWrapper(Authentication original, Collection<GrantedAuthority> extraRoles) {
      this.original = original;
      this.extraRoles = extraRoles;
   }

	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> originalRoles = original.getAuthorities();
		Collection<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.addAll(originalRoles);
		roles.addAll(extraRoles);
      return roles;
   }

   public String getName() { return original.getName(); }
   public Object getCredentials() { return original.getCredentials(); }
   public Object getDetails() { return original.getDetails(); }   
   public Object getPrincipal() { return original.getPrincipal(); }
   public boolean isAuthenticated() { return original.isAuthenticated(); }

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
      original.setAuthenticated( isAuthenticated );
   }  
}
