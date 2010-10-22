package lt.walrus.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import lt.walrus.dao.UserServiceDao;
import lt.walrus.model.WalrusUser;
import lt.walrus.service.user.EmailConfig;
import lt.walrus.service.user.InviteCommand;
import lt.walrus.service.user.MassMailCommand;
import lt.walrus.service.user.PasswordChangeCommand;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.webflow.context.ExternalContext;

/**
 * Service for working with users of Walrus
 */
@Service("userService")
public class UserService implements UserDetailsService {
	/**
	 * user dao
	 */
	private UserServiceDao dao;
	/**
	 * mail service to send emails
	 */
	MailService mailService;
	/**
	 * email configuration data
	 */
	EmailConfig emailConfig;

	@Override
	/**
	 * returns user by username. Email is used as username.
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		return dao.getUserByEmail(username);
	}

	public UserServiceDao getDao() {
		return dao;
	}

	public void setDao(UserServiceDao dao) {
		this.dao = dao;
	}

	/**
	 * @param id
	 * @return user by id
	 */
	public WalrusUser loadUser(String id) {
		return dao.loadUser(id);
	}

	/**
	 * Delete user from the system
	 * 
	 * @param user
	 */
	public void delete(WalrusUser user) {
		dao.delete(user);
	}

	/**
	 * Save user
	 * 
	 * @param user
	 */
	public void save(WalrusUser user) {
		dao.save(user);
	}

	public void setUserPassword(WalrusUser user, PasswordChangeCommand command) {
		setUserPassword(user, command.getPassword());
	}

	/**
	 * Sets user a password
	 * 
	 * @param user
	 * @param password
	 *            in plaintext
	 */
	public void setUserPassword(WalrusUser user, String password) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		user.setPassword(encoder.encodePassword(password, null));
	}

	@SuppressWarnings("unchecked")
	/**
	 * Creates a user (or several users) and sends them an invite code by email
	 */
	public void invite(InviteCommand command, ExternalContext context) {
		Set<String> emails = new HashSet(Collections.list(new StringTokenizer(command.getEmails())));
		Iterator<String> i = emails.iterator();
		while (i.hasNext()) {
			String email = i.next();
			if (!emailExists(email)) {
				WalrusUser user = new WalrusUser();
				user.setRole(WalrusUser.ROLE_ADMIN);
				user.setEmail(email);
				Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				user.setInviteKey(encoder.encodePassword(String.valueOf(System.currentTimeMillis()), encoder));
				dao.persist(user);
				sendInvite(user, command.getComment(), (HttpServletRequest) context.getNativeRequest());
			}
		}
	}

	private boolean emailExists(String email) {
		return null != dao.getUserByEmail(email);
	}

	private void sendInvite(WalrusUser user, String comment, HttpServletRequest request) {
		String text = comment + emailConfig.getInviteText()
				+ "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/cms/register?key=" + user.getInviteKey() + "\n\n" + emailConfig.getDefaultFooter();
		try {
			mailService.sendEmail(user.getEmail(), emailConfig.getInviteEmailSubject(), text, emailConfig.getEmailFrom(), emailConfig.getEmailFromName());
		} catch (MailException ex) {
			logger.error("Can't send email to: " + user.getEmail(), ex);
		}
	}

	/**
	 * Sends welcome message and password to user
	 * 
	 * @param user
	 * @param password
	 */
	public void sendWelcome(WalrusUser user, String password) {
		final String body = String.format(emailConfig.getWelcomeTemplate(), user.getUsername(), password)		
				+ emailConfig.getDefaultFooter();
		try {
			mailService.sendEmail(user.getEmail(), emailConfig.getWelcomeEmailSubject(), body, emailConfig.getEmailFrom(), emailConfig.getEmailFromName());
		} catch (MailException ex) {
			logger.error("Can't send email to: " + user.getEmail(), ex);
		}
	}

	private static transient Logger logger = Logger.getLogger(UserService.class);

	/**
	 * Updates secority context to specified user. After user comes by invite
	 * URL and fills required data, she is automatically logged on using this
	 * method.
	 * 
	 * @param user
	 */
	public void updateSecurityContext(WalrusUser user) {
		if (null == user) {
			logger.debug("UpdSecCtx: no user specified");
			return;
		}
		SecurityContext ctx = SecurityContextHolder.getContext();
		Authentication auth = ctx.getAuthentication();
		if (null != auth) {
			WalrusUser loggedOnUser = getLoggedOnUser(auth);
			if (null == loggedOnUser) {
				logger.warn("Could not load logged on user!");
			} else {
				if (user.getId() == loggedOnUser.getId()) {
					logger.debug("UPDATING SECURITY CONTEXT!!!!");
					Authentication updatedAuth;
					if (auth instanceof RememberMeAuthenticationToken) {
						updatedAuth = new RememberMeAuthenticationToken("bukkit", user, user.getAuthorities());
					} else {
						updatedAuth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
					}
					logger.debug("UpdSecCtx: SET AUTH: " + updatedAuth);
					ctx.setAuthentication(updatedAuth);

				} else {
					logger.debug("updating other user, do nothing");
				}
			}
		}
	}

	private WalrusUser getLoggedOnUser(Authentication auth) {
		if (auth.getPrincipal() instanceof WalrusUser) {
			return (WalrusUser) auth.getPrincipal();
		} else {
			try {
				return dao.loadUser((String) auth.getPrincipal());
			} catch (Exception e) {
				return null;
			}
		}
	}

	/**
	 * @param inviteKey
	 * @return user by invite key
	 */
	public WalrusUser getByInviteKey(String inviteKey) {
		WalrusUser user = dao.getByInviteKey(inviteKey);
		return user;
	}

	/**
	 * Mark user as registered.
	 * 
	 * @param user
	 */
	public void register(WalrusUser user) {
		user.setInviteKey(null);
	}

	/**
	 * Mass mail command filled with users by userSearchCriteria
	 * 
	 * @param userSearchCriteria
	 * @return
	 */
	public MassMailCommand createMassMailCommand(ValueListInfoCommand userSearchCriteria) {
		MassMailCommand c = new MassMailCommand();
		for (String userId : userSearchCriteria.getSelectedIds()) {
			WalrusUser u = dao.loadUser(userId);
			if(null != u) {
				dao.getHibernateTemplate().initialize(u);
				c.getUsers().add(u);
			}
		}
		return c;
	}

	/**
	 * Sends mass e-mail message
	 * 
	 * @param command
	 */
	public void sendMassMail(MassMailCommand command) {
		logger.debug("sending mass mail!");
		List<WalrusUser> users;
		if(command.getUsers().size() > 0) {
			users = command.getUsers();
		} else {
			users = dao.getAll();
		}
		for (WalrusUser u : users) {
			logger.debug("\n\n\nsengin email to: " + u.getEmail());
			mailService.sendEmail(u.getEmail(), command.getSubject(), command.getText(), emailConfig.getEmailFrom(), emailConfig.getEmailFromName());
		}
	}
	
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public EmailConfig getEmailConfig() {
		return emailConfig;
	}

	public void setEmailConfig(EmailConfig emailConfig) {
		this.emailConfig = emailConfig;
	}
}
