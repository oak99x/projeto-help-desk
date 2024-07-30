package mfreitas.msemail.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;

import mfreitas.msemail.models.Email;

@Service
public class EmailService {

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.secret}")
    private String apiSecret;

    @Value("${default.domain.email}")
    private String defaultDomainEmail;

    @Value("${default.domain.name}")
    private String defaultDomainName;



    public void sendEmail(Email e) throws MailjetException, MailjetSocketTimeoutException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(apiKey, apiSecret, new ClientOptions("v3.1"));

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                    .put(new JSONObject()
                        .put(Emailv31.Message.FROM, new JSONObject()
                            .put("Email", defaultDomainEmail)
                            .put("Name", defaultDomainName))
                        .put(Emailv31.Message.TO, new JSONArray()
                            .put(new JSONObject()
                                .put("Email", e.getToUser().getEmail())
                                .put("Name", e.getToUser().getName())))
                        .put(Emailv31.Message.TEMPLATEID, 5809204)
                        .put(Emailv31.Message.TEMPLATELANGUAGE, true)
                        .put(Emailv31.Message.SUBJECT, "[[data:name:"+defaultDomainName+"]]")
                        .put(Emailv31.Message.VARIABLES, new JSONObject()
                            .put("name", "")
                            .put("procedure", e.getProcedure() + " - " + e.getTicketId())
                            .put("date", e.getStartDate())
                            .put("generator", e.getGenerator())
                            .put("requester", e.getRequester())
                            .put("responsible", e.getResponsible())
                            .put("status", e.getStatus())
                            .put("ticketId", e.getTicketId())
                            .put("title", e.getTitle())
                            .put("description", e.getDescription()))));
        
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }

}
