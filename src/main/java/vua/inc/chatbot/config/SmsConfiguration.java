package vua.inc.chatbot.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("africastalking")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class SmsConfiguration {

    private String username;
    private String apiKey;


}
