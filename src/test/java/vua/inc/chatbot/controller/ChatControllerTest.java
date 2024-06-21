package vua.inc.chatbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vua.inc.chatbot.controller.ChatController;
import vua.inc.chatbot.model.SMS;
import vua.inc.chatbot.model.dtos.IncomingSmsDTO;
import vua.inc.chatbot.model.dtos.SmsRequest;
import vua.inc.chatbot.model.dtos.SmsResponse;
import vua.inc.chatbot.service.VuaChatService;
import vua.inc.chatbot.service.sms.SmsService;

import java.util.List;

@SpringBootTest
public class ChatControllerTest {

    @Mock
    private SmsService smsService;

    @Mock
    private VuaChatService vuaChatService;

    @InjectMocks
    private ChatController chatController;

    @Test
    public void SendSms_ReturnsCorrectResponse() {
        SmsRequest smsRequest = new SmsRequest( "Hello", new String[]{"1234567890"});
        SmsResponse expectedResponse = new SmsResponse("Message sent successfully");
        when(smsService.sendSms(smsRequest)).thenReturn(expectedResponse);

        ResponseEntity<SmsResponse> response = chatController.SendSms(smsRequest);

        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void ProcessIncomingSms_CallsServiceWithCorrectData() {
        IncomingSmsDTO smsPayload = new IncomingSmsDTO("1", "2023-01-01", "1234567890", "linkId", "Hello", "9876543210", "networkCode");

        chatController.processIncomingSms(smsPayload);

        verify(smsService).processIncomingSms(smsPayload);
    }

    @Test
    public void GetAllSms_ReturnsSortedSmsList() {
        List<SMS> expectedSmsList = List.of(
                new SMS("1", "2023-01-01", "1234567890", "linkId", "Hello", "9876543210", "networkCode", null, null),
                new SMS("2", "2023-01-02", "1234567891", "linkId2", "Hi", "9876543211", "networkCode2", null, null)
        );
        when(smsService.getAllSms()).thenReturn(expectedSmsList);

        ResponseEntity<List<SMS>> response = chatController.getAllSms();

        assertEquals(ResponseEntity.ok(expectedSmsList), response);
    }
}