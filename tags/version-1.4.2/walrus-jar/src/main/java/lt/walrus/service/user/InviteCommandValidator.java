package lt.walrus.service.user;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;

public class InviteCommandValidator {
	static final transient Logger logger = Logger.getLogger(InviteCommandValidator.class);
	public void validateInviteUsers(InviteCommand command, MessageContext context) {
		if(!StringUtils.hasLength(command.getEmails())) {
			context.addMessage(new MessageBuilder().error().defaultText("Turite nurodyti bent vieną e-pašto adresą").build());
			return;
		}
		
		StringTokenizer t = new StringTokenizer(command.getEmails());

		while (t.hasMoreTokens()) {
			String email = t.nextToken();
			EmailAddress a = new EmailAddress(email);
			logger.debug("validating email " + email);
			if(!a.isValid()) {
				context.addMessage(new MessageBuilder().error().defaultText("Nurodytas adresas \"" + email +"\" neatitinka e-pašto adresų standarto. Ištrinkite arba pataisykite jį ir mėginkite dar kartą.").build());
			}
		}
	}
}
