package lt.walrus.service.user;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

public class PasswordChangeCommandValidator {
	protected transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	public void validateChangePassword(PasswordChangeCommand command, MessageContext context) {
		logger.debug("PasswordChangeCommandValidator.validateChangePassword: \n\n\n\n\n VALIDATING!!!! \n\n\n\n " + command.getPassword() + "=="
				+ command.getRepeatPassword());
		if (!StringUtils.hasText(command.getPassword())) {
			context.addMessage(new MessageBuilder().error().source("noPassword").defaultText("Nenurodytas slaptažodis!").build());
		}
		if (!StringUtils.hasText(command.getRepeatPassword())) {
			context.addMessage(new MessageBuilder().error().source("noRepeatedPassword").defaultText("Nepakartotas slaptažodis!").build());
		}

		if (!command.getRepeatPassword().equals(command.getPassword())) {
			context.addMessage(new MessageBuilder().error().source("passwordsDontMatch").defaultText("Nurodyti slaptažodžiai nesutampa!").build());
		}

	}
}
