package vua.inc.chatbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import vua.inc.chatbot.model.Answer;
import vua.inc.chatbot.model.Question;
import vua.inc.chatbot.model.SMS;
import vua.inc.chatbot.model.dtos.IncomingSmsDTO;
import vua.inc.chatbot.model.dtos.SmsRequest;
import vua.inc.chatbot.model.dtos.SmsResponse;
import vua.inc.chatbot.repo.ChatContextRepository;
import vua.inc.chatbot.service.VuaChatService;
import vua.inc.chatbot.service.sms.SmsService;
import vua.inc.chatbot.utils.AppUtils;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URI+"chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final VuaChatService vuaChatService;
    private final SmsService smsService;
    private final ChatContextRepository chatContextRepository ;


    @PostMapping("/send")
    public ResponseEntity<SmsResponse> SendSms(@RequestBody SmsRequest smsRequest) {
        log.warn("Send sms {}", smsRequest);
        return ResponseEntity.ok(smsService.sendSms(smsRequest));
    }


    /**
     *  receive sms route on this file
     *  the structure should be like /incoming
     *  @param smsPayload
     */

    @PostMapping(value = "/incoming", consumes = "application/x-www-form-urlencoded")
    public void processIncomingSms(@ModelAttribute IncomingSmsDTO smsPayload) {
        log.warn("Incoming sms {}", smsPayload);
//        System.out.println(smsPayload);
        smsService.processIncomingSms(smsPayload);
    }

    // get all sms
    @GetMapping
    public ResponseEntity<List<SMS>> getAllSms(){
        return ResponseEntity.ok(smsService.getAllSms()) ;
    }


    @PostMapping
    public ResponseEntity<Answer> ask(@RequestBody Question question, HttpServletRequest request){
        return ResponseEntity.ok(vuaChatService.generateResponse(question,request.getSession().getId()));
    }
  
}
