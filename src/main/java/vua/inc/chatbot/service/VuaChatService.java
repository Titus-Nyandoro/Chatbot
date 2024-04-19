package vua.inc.chatbot.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.ChatMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vua.inc.chatbot.model.Answer;
import vua.inc.chatbot.model.ChatContextExchange;
import vua.inc.chatbot.model.Question;
import vua.inc.chatbot.repo.ChatContextRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VuaChatService {
    private final VectorStore serviceVectorStore;
    private final OpenAiChatClient chatClient;
    private  final ChatContextRepository chatContextRepository;
    @Value("classpath:/system-info-prompt.st")
    private Resource systemInfoPrompt;

    // generate response
    public Answer generateResponse(Question question,  String sessionId){
        //retrieve context before answering
        String context="";

        List<String> chatContextExchanges = chatContextRepository.findAllPastHour(sessionId, Instant.now().minusMillis(60*60*60));
        if(chatContextExchanges.isEmpty() ){
            context = "";
        }else{
            context = String.join(" ",chatContextExchanges);
        }
        // retrieve similar docs
        List<Document> similarDoc =  serviceVectorStore.similaritySearch(
                SearchRequest
                        .query(question.question())
                        .withTopK(3)); // specify the list length of similarity output
        UserMessage userMessage= new UserMessage(question.question());
        Message systemMessage = getSystemMessage(similarDoc, context); // pass retrieved similarity outcomes

        // build prompt
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        ChatResponse response = chatClient.call(prompt);
        return new Answer(response.getResult().getOutput().getContent());
    }
    // get system messages
    private Message getSystemMessage(List<Document> doc, String chatContext){
        String docs =chatContext+doc.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        PromptTemplate promptTemplate = new PromptTemplate(systemInfoPrompt);
        return promptTemplate.createMessage(Map.of("documents", docs));
    }

    public String askQuestion(String question) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        Answer answer = generateResponse(Question.builder().question(question).build(), attributes.getSessionId() );
        return answer.answer();
    }

}
