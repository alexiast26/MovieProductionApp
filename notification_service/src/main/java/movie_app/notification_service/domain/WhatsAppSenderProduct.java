package movie_app.notification_service.domain;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class WhatsAppSenderProduct implements NotificationSender {
    private final String accountSid;
    private final String authToken;
    private final String fromNumber;
    private final String toNumber;

    public WhatsAppSenderProduct(String accountSid, String authToken, String fromNumber, String toNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
    }

    @Override
    public void send(String messageContent) {
        Twilio.init(accountSid, authToken);
        try {
            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    "*Cinematics Notif* \n\nHey there! You have a new notification \n" + messageContent
            ).create();

            System.out.println("WhatsApp product sent successfully! ID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Error sending WhatsApp message: " + e.getMessage());
        }
    }
}