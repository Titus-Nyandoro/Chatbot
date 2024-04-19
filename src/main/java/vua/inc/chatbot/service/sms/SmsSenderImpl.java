package vua.inc.chatbot.service.sms;

import com.africastalking.AfricasTalking;
import com.africastalking.ChatService;
import com.africastalking.SmsService;
import com.africastalking.sms.Recipient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import vua.inc.chatbot.config.SmsConfiguration;
import vua.inc.chatbot.model.Answer;
import vua.inc.chatbot.model.ChatContextExchange;
import vua.inc.chatbot.model.Question;
import vua.inc.chatbot.model.SMS;
import vua.inc.chatbot.model.dtos.IncomingSmsDTO;
import vua.inc.chatbot.model.dtos.SmsRequest;
import vua.inc.chatbot.model.dtos.SmsResponse;
import vua.inc.chatbot.repo.ChatContextRepository;
import vua.inc.chatbot.repo.SmsRepository;
import vua.inc.chatbot.service.VuaChatService;
import vua.inc.chatbot.utils.AppUtils;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("africastalking")
@Slf4j
@RequiredArgsConstructor
public class SmsSenderImpl implements SmsSender {
    private final ChatContextRepository chatContextRepository;
    private final SmsConfiguration smsConfiguration;
    private final SmsRepository smsRepository;
    private final VuaChatService chatService;
    // Define the regular expression for the phone number
    private static final String PHONE_REGEX = "^\\+2547\\d{8}$";
    private static final Pattern pattern = Pattern.compile(PHONE_REGEX);
    @Override

    public SmsResponse sendSms(SmsRequest smsRequest) {


        SmsResponse smsResponse = null;
        /**
         * TODO sms 3:
         *   1. create a function that will allow all phone numbers in the account to be sent sms
         */
            String from = "20384";
            String message = smsRequest.message();
            String[] recipients = smsRequest.recipients();
            List<String> formattedRecipients = new ArrayList<>();
            // validate phone
            for (String phone: recipients){
                String formattedPhoneNumber = reformatPhoneNumber(phone);
                if (isValidPhoneNumber(formattedPhoneNumber)){
                   formattedRecipients.add(formattedPhoneNumber);
                }else {
                    return SmsResponse.builder()
                            .message("Titus We have trouble processing recipients sms")
                            .build();
                }
            }

            SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
            try {
                List<Recipient> response = sms.send(message, from, formattedRecipients.toArray(new String[0]), true);

                for (int i=0; i<response.size(); i++) {
                    // save message to db
                    SMS outgoing = SMS.builder()
                            .id(UUID.randomUUID().toString())
                            .clintFrom(from)
                            .createdAt(LocalDateTime.now())
                            .networkCode(AppUtils.VUA_SUCCESS_NETWORK_RESPONSE_CODE)
                            .receiver(response.get(i).number)
                            .text(message)
                            .build();
                    //save sms and associate an account being sent sms with the sms
//                    saveAndAssociateSmsToAccount(response.get(i).number, accKeys[i], outgoing);
                    smsResponse = SmsResponse.builder()
                            .message("Message sent to "+response.get(i).number+" and the status is: "+ response.get(i).status)
                            .build();
                }

            } catch (Exception ex) {
                // save error message to db
                SMS outgoing = SMS.builder()
                        .id(UUID.randomUUID().toString())
                        .clintFrom(from)
                        .createdAt(LocalDateTime.now())
                        .networkCode(AppUtils.VUA_FAILURE_NETWORK_RESPONSE_CODE)
                        .receiver(recipients[0])
                        .text(ex.getMessage())
                        .build();
                
//                saveAndAssociateSmsToAccount(recipients[0], smsRequest.accKeys()[0], outgoing);
                // return
                smsResponse = SmsResponse.builder()
                        .message(ex.getMessage())
                        .build();
            }


            return smsResponse;
        }

    /**
     * Checks if the phone number is valid.
     *
     * @param phone the phone number to check
     * @return true if the phone number is valid, false otherwise
     */
    private boolean isValidPhoneNumber(String phone) {
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    @Override
    public void processIncomingSms(IncomingSmsDTO sms) {

        // generate sms and send it to user
        Answer answer = chatService.generateResponse(Question.builder()
                .question(sms.text()).build(), sms.from());
        log.info("sending sms to > ", sms.from());

        sendSms(new SmsRequest(answer.answer(),new String[]{sms.from()}));
        log.info("sms sent to > ", sms.from());
    }


    /**
     * Reformats the given phone number to the standard +254 format.
     *
     * @param phone the phone number to reformat
     * @return the reformatted phone number
     */
    public String reformatPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return phone;
        }

        // Regex pattern to identify phone numbers that start with 07, 011, or 254
        Pattern pattern = Pattern.compile("^(07\\d{8}|011\\d{7}|2547\\d{8})$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.find()) {
            StringBuilder formattedPhone = new StringBuilder("+254");

            if (phone.startsWith("2547")) {
                // If the number starts with 2547, append the rest of the number after 254
                formattedPhone.append(phone.substring(3));
            } else if (phone.startsWith("07") || phone.startsWith("011")) {
                // If the number starts with 07 or 011, remove the initial 0 and append the rest
                formattedPhone.append(phone.substring(1));
            }
            return formattedPhone.toString();
        }

        // If the phone number does not match any expected format, return it as is
        return phone;
    }


}
