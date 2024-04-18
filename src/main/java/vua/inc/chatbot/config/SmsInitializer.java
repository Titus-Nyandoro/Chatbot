package vua.inc.chatbot.config;

import com.africastalking.AfricasTalking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SmsInitializer {

    @Autowired
    public SmsInitializer(SmsConfiguration smsConfiguration) {
        AfricasTalking.initialize(smsConfiguration.getUsername(), smsConfiguration.getApiKey());

        log.info("AT initialized with ApiKey {} ",  smsConfiguration.getApiKey());

    }
}
