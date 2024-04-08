package vua.inc.chatbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vua.inc.chatbot.model.Answer;
import vua.inc.chatbot.model.Question;
import vua.inc.chatbot.service.ChatService;
import vua.inc.chatbot.utils.AppUtils;

@RestController
@RequestMapping(AppUtils.BASE_URI+"chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Answer> ask(@RequestBody Question question){
        return ResponseEntity.ok(chatService.generateResponse(question));
    }
}
