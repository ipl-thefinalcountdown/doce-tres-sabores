package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class EmailBean {
	@Resource(name = "java:jboss/mail/Default") Session session;

	public void send(String to, String subject, String text) {
		try {
			Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(text);

			Transport.send(message);
		} catch (MessagingException e) {
			Logger.getLogger(EmailBean.class.getName()).log(Level.WARNING, e.getMessage());
		}
	}
}
