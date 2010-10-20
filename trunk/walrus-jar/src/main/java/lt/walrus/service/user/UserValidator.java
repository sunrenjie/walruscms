package lt.walrus.service.user;

import lt.walrus.dao.UserServiceDao;
import lt.walrus.model.WalrusUser;

import org.apache.log4j.Logger;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

public class UserValidator {
	static final transient Logger logger = Logger.getLogger(UserValidator.class);
	private UserServiceDao dao;

	public UserServiceDao getDao() {
		return dao;
	}

	public void setDao(UserServiceDao dao) {
		this.dao = dao;
	}

	public void validateEditUserProfile(WalrusUser user, MessageContext context) {
		if (!StringUtils.hasLength(user.getFirstName()) || !StringUtils.hasLength(user.getLastName()) || !StringUtils.hasLength(user.getCompany())
				|| !StringUtils.hasLength(user.getCompanyAddress()) || !StringUtils.hasLength(user.getEmail()) || !StringUtils.hasLength(user.getPhone())
				|| !StringUtils.hasLength(user.getPosition())) {
			context.addMessage(new MessageBuilder().error().source("fieldsNotFilled").defaultText("Užpildykite visus formos laukus!").build());
			return;
		}
		
		EmailAddress a = new EmailAddress(user.getEmail());
		logger.debug("\n\n\n\n user validator validating email " + user.getEmail() + " \n\n\n is valid: " + a.isValid());
		if(!a.isValid()) {
			context.addMessage(new MessageBuilder().error().defaultText("Nurodytas e-pašto adresas neatitinka e-pašto adresų standarto. Patikrinkite ir pataisykite.").build());
			return;
		}
		WalrusUser userFromDao = (WalrusUser) dao.getUserByEmail(user.getEmail());
		if (null != userFromDao && userFromDao.getId() != user.getId()) {
			context
					.addMessage(new MessageBuilder()
							.error()
							.source("emailNotUnique")
							.defaultText(
									"E-pašto adresas "
											+ user.getEmail()
											+ " jau yra priskirtas kitam vartotojui. Negalima priskirti to pačio e-pašto adreso keliems vartotojams. Nurodykite kitą e-pašto adresą arba ištrinkite vartotoją, turintį šį e-pašto adresą.")
							.build());
		}
		if (isCurrentAuthenticatedUser(user) && !userFromDao.getRole().equals(user.getRole())) {
			context.addMessage(new MessageBuilder().error().source("fieldsNotFilled").defaultText("Jūs negalite keisti savo rolės!").build());
		}
		dao.evict(userFromDao);
	}

	private boolean isCurrentAuthenticatedUser(WalrusUser user) {
		return SecurityContextHolder.getContext().getAuthentication().getName().equals(user.getUsername());
	}

	public void validateRegistrationForm(WalrusUser user, MessageContext context) {
		validateEditUserProfile(user, context);

		if (!StringUtils.hasLength(user.getPassword())) {
			context.addMessage(new MessageBuilder().error().source("passwordNotFilled").defaultText("Nurodykite savo būsimą slaptažodį").build());
			return;
		}

		if (!user.getPassword().equals(user.getPasswordRepeat())) {
			context.addMessage(new MessageBuilder().error().source("passwordsDontMatch").defaultText(
					"Slaptažodžiai nesutampa. Nurodykite tą patį slaptažodį abejuose laukeliuose.").build());
		}
	}
}
