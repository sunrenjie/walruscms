package lt.walrus.service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 * Email related service
 */
@Service("mailService")
public class MailService {
	/**
	 * Spring framework mail sender
	 */
	JavaMailSender mailSender;

	/**
	 * Sends email message
	 * 
	 * @param emailTo
	 *            email address
	 * @param subject
	 * @param text
	 * @param emailFrom
	 *            from email address
	 * @param emailFromName
	 *            from name
	 * @throws MailException
	 */
	public void sendEmail(String emailTo, String subject, String text, String emailFrom, String emailFromName) throws MailException {
		final String body = text;
		final String to = emailTo;
		final String fSubject = subject;
		final String from = emailFrom;
		final String fromName = emailFromName;
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.addHeader("Content-Type", "text/plain; charset=\"UTF-8\"");
				mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
				mimeMessage.setText(body, "UTF-8");
				mimeMessage.setSubject(fSubject, "UTF-8");
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
				mimeMessage.setSender(new InternetAddress(from, fromName, "UTF-8"));
				mimeMessage.setFrom(new InternetAddress(from, fromName, "UTF-8"));
			}
		};
		mailSender.send(preparator);
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
}
