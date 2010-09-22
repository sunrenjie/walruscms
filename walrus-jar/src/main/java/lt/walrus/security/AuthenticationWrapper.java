package lt.walrus.security;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;

/**
 * Authentication wrapper that makes it easy to add additional roles to
 * authentication
 */
public class AuthenticationWrapper implements Authentication
{
	private static final long serialVersionUID = 3404314781040339823L;
private Authentication original;
   private GrantedAuthority[] extraRoles;

   public AuthenticationWrapper( Authentication original, GrantedAuthority[] extraRoles )
   {
      this.original = original;
      this.extraRoles = extraRoles;
   }

   public GrantedAuthority[] getAuthorities()
   {
      GrantedAuthority[] originalRoles = original.getAuthorities();
      GrantedAuthority[]  roles = new GrantedAuthority[originalRoles.length + extraRoles.length];
      System.arraycopy( originalRoles, 0, roles, 0, originalRoles.length );
      System.arraycopy( extraRoles, 0, roles, originalRoles.length, extraRoles.length );
      return roles;
   }

   public String getName() { return original.getName(); }
   public Object getCredentials() { return original.getCredentials(); }
   public Object getDetails() { return original.getDetails(); }   
   public Object getPrincipal() { return original.getPrincipal(); }
   public boolean isAuthenticated() { return original.isAuthenticated(); }
   public void setAuthenticated( boolean isAuthenticated ) throws IllegalArgumentException
   {
      original.setAuthenticated( isAuthenticated );
   }  
}
