package vua.inc.chatbot.service.sms;

import vua.inc.chatbot.model.dtos.IncomingSmsDTO;
import vua.inc.chatbot.model.dtos.SmsRequest;
import vua.inc.chatbot.model.dtos.SmsResponse;

public interface SmsSender {

    SmsResponse sendSms(SmsRequest smsRequest);

    void processIncomingSms(IncomingSmsDTO object);
}
