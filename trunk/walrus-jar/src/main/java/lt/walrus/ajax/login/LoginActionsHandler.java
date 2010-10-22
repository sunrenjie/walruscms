package lt.walrus.ajax.login;

import java.util.HashMap;
import java.util.Random;

import lt.walrus.model.WalrusUser;
import lt.walrus.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class LoginActionsHandler extends AbstractAjaxHandler {
	private static final transient Logger logger = Logger.getLogger(LoginActionsHandler.class);

	private JavaMailSender mailSender;
	private UserService userService;

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public AjaxResponse resetPassword(AjaxEvent e) {
		AjaxResponse response = new AjaxResponseImpl("UTF-8");
		String email = e.getParameters().get("email");
		HashMap<String, Object> jsParams = new HashMap<String, Object>();

		if (StringUtils.hasLength(email)) {
			WalrusUser user = (WalrusUser) userService.loadUserByUsername(email);
			if (null != user) {
				String password = generateRandomPassword();
				userService.setUserPassword(user, password);
				userService.save(user);
				userService.sendWelcome(user, password);
				response.addAction(new ExecuteJavascriptFunctionAction("passwordReset", jsParams));
			} else {
				jsParams.put("message", "There is no such email address in the database. Please check the address you specified.");
				response.addAction(new ExecuteJavascriptFunctionAction("passwordResetError", jsParams));
			}
		} else {
			jsParams.put("message", "You must specify your email address.");
			response.addAction(new ExecuteJavascriptFunctionAction("passwordResetError", jsParams));
		}
		return response;
	}

	public static String generateRandomPassword() {
		int size = 6;
		Random rnd = new Random();

		String[] consonants = { "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "qu", "r", "s", "t", "v", "w", "x", "z", "ch", "cr", "fr", "nd",
				"ng", "nk", "nt", "ph", "pr", "rd", "sh", "sl", "sp", "st", "th", "tr" };
		String[] vocals = { "a", "e", "i", "o", "u", "y" };

		boolean alternate = true;
		String password = "";
		String chunk = "";
		for (int i = 0; i < size; i++) {
			chunk = alternate ? consonants[rnd.nextInt(consonants.length)] : vocals[rnd.nextInt(vocals.length)];
			alternate = !alternate;
			password += chunk;
		}
		return password;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
