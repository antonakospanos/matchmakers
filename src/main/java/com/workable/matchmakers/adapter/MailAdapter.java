package com.workable.matchmakers.adapter;

import com.workable.matchmakers.configuration.MailConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.email.EmailPopulatingBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailAdapter {

    private Mailer mailer;
    private byte[] logo;
    private String admins;
    private ResourceLoader resourceLoader;

    @Autowired
    MailAdapter(MailConfiguration mailConfiguration, ResourceLoader resourceLoader) {
        String host = mailConfiguration.getFrom().getHost();
        int port = mailConfiguration.getFrom().getPort();
        String username = mailConfiguration.getFrom().getUsername();
        String password = mailConfiguration.getFrom().getPassword();

        this.admins = mailConfiguration.getTo().getAdmins();
        this.mailer = MailerBuilder
                .withSMTPServer(host, port, username, password)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .clearEmailAddressCriteria()
                // .withDebugLogging(true)
                .buildMailer();
        this.resourceLoader = resourceLoader;
        loadLogo();
    }

    private void loadLogo() {
        try {
            this.logo = IOUtils.toByteArray(resourceLoader.getResource("classpath:img/workable.png").getInputStream());
        } catch (Exception e) {
            log.warn("Could not load email logo cause: " + e);
        }
    }

    public void send(String from, String to, String cc, String bcc, String subject, String body) {
        EmailPopulatingBuilder emailBuilder = EmailBuilder.startingBlank()
                .from(from)
                .to(to);

        if (cc != null) {
            emailBuilder = emailBuilder.cc(cc);
        }
        if (bcc != null) {
            emailBuilder = emailBuilder.bcc(bcc);
        }
        if (this.logo != null) {
            emailBuilder = emailBuilder.withEmbeddedImage("Workable", this.logo, "image/png");
        }

        try {
            Email email =  emailBuilder
                    //.withReplyTo("lollypop", "lolly.pop@othermail.com")
                    .withSubject(subject)
                    .withHTMLText(body)
                    // .withPlainText("Please view this email in a modern email client!")
                    .buildEmail();

            mailer.sendMail(email);
            log.info("\n\nSent email to '" + to + "'\n\nSubject:\n" + subject + "\n\nMessage:\n" + body + "\n");

        } catch (Exception e) {
            String errorMailSubject = "Mail delivery failed: " + subject;

            String errorMailBody = "Hello team," +
                    "<br><br>" +
                    "The following email to '<b>" + to + "</b>' failed to be dispatched, please confirm that the user's email address is valid!" +
                    "<br><br>" +
                    body;

            Email emailMail =  emailBuilder
                    .from(from)
                    .to(admins)
                    .withSubject(errorMailSubject)
                    .withHTMLText(errorMailBody)
                    .withEmbeddedImage("Workable", logo, "image/png")
                    .buildEmail();

            try {
                mailer.sendMail(emailMail);
                log.error("\n\nFailed to send email '"+subject+"' to " + to, e);
            } catch (Exception ex) {
                ex.addSuppressed(e);
                log.error("\n\nFailed to send Email Delivery Failure Notification to administrators [" + admins + "]", ex);
            }
        }
    }
}
