package vua.inc.chatbot.service.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vua.inc.chatbot.model.SMS;
import vua.inc.chatbot.model.dtos.IncomingSmsDTO;
import vua.inc.chatbot.model.dtos.SmsRequest;
import vua.inc.chatbot.model.dtos.SmsResponse;
import vua.inc.chatbot.repo.SmsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmsService {
    private final SmsRepository smsRepository;
    private final SmsSenderImpl smsSenderImpl;
    @Autowired
    public SmsService(@Qualifier("africastalking") SmsSenderImpl smsSenderImpl,SmsRepository smsRepository ) {
        this.smsSenderImpl = smsSenderImpl;
        this.smsRepository = smsRepository;
    }
    public SmsResponse sendSms(SmsRequest smsRequest) {

        return smsSenderImpl.sendSms(smsRequest);
    }

    public void processIncomingSms(IncomingSmsDTO smsPayload) {
        smsSenderImpl.processIncomingSms(smsPayload);
    }

    public List<SMS> getAllSms() {
        List<SMS> allSms = (List<SMS>) smsRepository.findAll(); // Assuming you have a method to fetch all SMS records
        allSms.sort((sms1, sms2) -> {
            LocalDateTime date1 = sms1.getUpdatedAt() != null ? sms1.getUpdatedAt() : sms1.getCreatedAt();
            LocalDateTime date2 = sms2.getUpdatedAt() != null ? sms2.getUpdatedAt() : sms2.getCreatedAt();
            return date1.compareTo(date2);
        });
        return allSms;
    }
}
