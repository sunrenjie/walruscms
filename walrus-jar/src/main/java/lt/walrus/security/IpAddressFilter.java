package lt.walrus.security;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.utils.WalrusSecurity;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.memory.UserAttribute;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A Spring Security filter that adds additional roles to Authentication object
 * if the request comes from allowed IP or network.
 */
public class IpAddressFilter extends OncePerRequestFilter {
	private static final String DEFAULT_ROLE = "ROLE_KNOWN_NETWORK";

	String role = DEFAULT_ROLE;
	List<String> allowedIps;
	Map<String, String> allowedNetworks;
	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		InetAddress ipAddress = InetAddress.getByName(request.getRemoteAddr());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(null == auth) {
			auth = createAnonymousAuthentication();
			SecurityContextHolder.getContext().setAuthentication(auth);
		} else if(WalrusSecurity.hasRole(getRole())) {
			filterChain.doFilter(request, response);
			return;
		}
		
		boolean allowed = false;
		if(null != allowedIps) {
			for (String  addr : allowedIps) {
				if(request.getRemoteAddr().equals(addr)) {
					allowed = true;
					addAdditionalRole(auth);
				}
			}
		}
		if(!allowed && null != allowedNetworks) {
			for (String  network : allowedNetworks.keySet()) {
				String netmask = allowedNetworks.get(network);
				if(InetAddressUtil.contains(InetAddress.getByName(network), InetAddress.getByName(netmask), ipAddress)) {
					allowed = true;
					addAdditionalRole(auth);
				}
			}
		}
		filterChain.doFilter(request, response);
	}


	private void addAdditionalRole(Authentication auth) {
		logger.debug("IpAddressFilter.addAdditionalRole: " + role);
		SecurityContextHolder.getContext().setAuthentication(new AuthenticationWrapper(auth, new GrantedAuthority[] {new GrantedAuthorityImpl(role)}));
	}


	private Authentication createAnonymousAuthentication() {
		Authentication auth;
		List <GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl("ROLE_ANONYMOUS"));
		
		UserAttribute u = new UserAttribute();
		u.setAuthorities(authorities);
		u.setPassword("guest");
		auth = new AnonymousAuthenticationToken("px", u.getPassword(), u.getAuthorities());
		logger.debug("IpAddressFilter.createAnonymousAuthentication: " + auth.getAuthorities());
		return auth;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public List<String> getAllowedIps() {
		return allowedIps;
	}


	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}


	public Map<String, String> getAllowedNetworks() {
		return allowedNetworks;
	}


	public void setAllowedNetworks(Map<String, String> allowedNetworks) {
		this.allowedNetworks = allowedNetworks;
	}

}
