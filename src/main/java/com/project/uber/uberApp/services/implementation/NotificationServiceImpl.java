package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.entities.Ride;
import com.project.uber.uberApp.services.NotificationService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${app.twilio.sid}")
    private String TWILIO_ACCOUNT_SID;

    @Value("${app.twilio.auth.token}")
    private String TWILIO_AUTH_TOKEN;

    @Value("${app.twilio.phone}")
    private String TWILIO_PHONE_NO;

    @Override
    public void sendOtpToRider(Ride ride, String otp) {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(ride.getRider().getUser().getPhone()),
                new com.twilio.type.PhoneNumber(TWILIO_PHONE_NO),
                "Your ride has been accepted by "+ride.getDriver().getUser().getName()+" and the OTP for your ride is "+otp)
                        .create();

        log.info(message.getSid());
    }
}
