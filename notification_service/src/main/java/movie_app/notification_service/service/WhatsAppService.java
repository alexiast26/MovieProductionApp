package movie_app.notification_service.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import movie_app.notification_service.domain.NotificationSender;
import movie_app.notification_service.domain.WhatsAppSenderProduct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService extends NotificationFactory {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    @Value("${twilio.whatsapp.to}")
    private String toNumber;


    @Override
    public NotificationSender createSender() {
        return new WhatsAppSenderProduct(accountSid, authToken, fromNumber, toNumber);
    }
}