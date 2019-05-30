package com.workable.matchmakers.service;

import com.workable.matchmakers.adapter.MailAdapter;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.dao.model.User;
import com.workable.matchmakers.configuration.MailConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailService {

    public static final String CANDIDATE_REGISTRATION_UPDATE = "/swagger-ui.html#/Candidates/updateRegistrationUsingPUT";
    public static final String CANDIDATE_INFO = "/swagger-ui.html#/Candidates/listAccountUsingGET_1";

    @Value("${application.url.backend}")
    private String backendUrl;

    @Value("${application.url.frontend}")
    private String frontendUrl;

    @Autowired
    private MailAdapter mailAdapter;

    private MailConfiguration mailConfiguration;
    private String addressFrom;

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    @Autowired
    public MailService(MailConfiguration mailConfiguration) {
        this.addressFrom = mailConfiguration.getFrom().getAddress();
        this.mailConfiguration = mailConfiguration;
    }

    public void sendCandidateRegistrationIsPending(Candidate candidate) {
        String subject = "Matchmakers <> " + candidate.getName() + ": Candidate Registration is Pending!";

        String body = "Hello team!" +
                "<br><br>" +
                "We have a new candidate in the platform with id '<b>" + candidate.getExternalId() + "</b>'." +
                "<br><br>" +
                "Please validate, in the next 24h, the registration of <i>" + candidate.getName() + "</i>" +
                " <a href=" + backendUrl + CANDIDATE_REGISTRATION_UPDATE + ">here</a>." +
                "<br><br>" +
                "Matchmakers";

        threadPool.execute(() -> {
            mailAdapter.send(addressFrom, mailConfiguration.getTo().getRegistration().getPending(), null, null, subject, body);
        });
    }

    public void sendCandidateRegistrationIsValidated(Candidate candidate) {
        String subject = "Matchmakers <> " + candidate.getName() + ": Candidate Registration is Validated!";

        String body = "Hi "+candidate.getName()+"," +
                "<br><br>" +
                "We're delighted to have you onboard!" +
                "<br>" +
                "You can now continue building your profile in order to receive interview requests from companies. " +
                "Feel free to contact us at <a href=\"mailto:support@workable.com\">support@workable.com</a> if you have any questions." +
                "<br><br>" +
                "Thanks," +
                "<br>" +
                "Team Matchmakers";

        threadPool.execute(() -> {
            mailAdapter.send(addressFrom, candidate.getEmail(), null, mailConfiguration.getTo().getRegistration().getValidated(), subject, body);
        });
    }

    public void sendCandidateRegistrationIsCompleted(Candidate candidate) {
        String subject = "Matchmakers <> " + candidate.getName() + ": Candidate Registration is Completed!";

        String body = "Hello team!" +
                "<br><br>" +
                "The registration of candidate <i>" + candidate.getName() + "</i> with id '<b>" + candidate.getExternalId() + "</b>' is completed in the platform." +
                "<br><br>" +
                "You may find the registration information " +
                " <a href=" + backendUrl + CANDIDATE_INFO + ">here</a>." +
                "<br><br>" +
                "Matchmakers";

        threadPool.execute(() -> {
            mailAdapter.send(addressFrom, mailConfiguration.getTo().getRegistration().getCompleted(), null, null, subject, body);
        });
    }

    public void sendCandidateWelcome(Candidate candidate) {
        String subject = "Matchmakers <> " + candidate.getName();

        String body = "Hello "+candidate.getName()+"!" +
                "<br><br>" +
                "Your registration in " + "<a href=" + frontendUrl + ">Matchmakers</a> is completed. " +
                "We will let you know as soon as a company is matched with your profile!" +
                "<br><br>" +
                "Welcome aboard," +
                "<br><br>" +
                "Matchmakers";

        threadPool.execute(() -> {
            mailAdapter.send(addressFrom, candidate.getEmail(), null, null, subject, body);
        });
    }

    public void sendPasswordReset(User user, String password) {
        String subject = "Matchmakers <> " + user.getName();

        String body = "Hi "+user.getName()+"," +
                "<br><br>" +
                "We’ve reset your password to '<b>"+password+"</b>'. Please use your new password to log-in to " +
                "<a href=" + frontendUrl + ">Matchmakers</a>." +
                "<br><br>" +
                "Thanks,<br>" +
                "Team Matchmakers";

        threadPool.execute(() -> {
            mailAdapter.send(addressFrom, user.getEmail(), null, null, subject, body);
        });
    }
}
