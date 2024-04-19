package vua.inc.chatbot.controller;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;
import vua.inc.chatbot.model.Answer;
import vua.inc.chatbot.model.Question;
import vua.inc.chatbot.service.ChatService;
import vua.inc.chatbot.service.DocumentsDownloaderService;
import vua.inc.chatbot.utils.AppUtils;

@RestController
@RequestMapping(AppUtils.BASE_URI+"chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final DocumentsDownloaderService documentsDownloaderService;
    private final VectorStore vectorStore;
    @PostMapping
    public ResponseEntity<Answer> ask(@RequestBody Question question){
        return ResponseEntity.ok(chatService.generateResponse(question));
    }
    @PostConstruct
    public void init() throws IOException{
        documentsDownloaderService.load("https://wra.go.ke/download/flood-advisory-april-2024/?wpdmdl=3404&refresh=66212e01c8e8e1713450497&ind=1712666578735&filename=Flood%20Advisory%20April%202024.pdf");
     
        // if(Files.exists(Paths.get("src/main/resources/downloaded.pdf"))){System.out.println("hello me");}
        
		System.out.println("called post");
      
        // Thread.sleep(3000);
       String path = "/home/macharia/projects/vua/ai2/Chatbot/src/main/resources/downloaded.pdf"; // Absolute filesystem path
       Resource resource = new FileSystemResource(path);

       if (resource.contentLength() <= 0) {
        throw new IllegalArgumentException("PDF file is blank or empty.");
    }
       PagePdfDocumentReader paragraphPdfDocumentReader = new PagePdfDocumentReader(resource);
        List<Document> docs = paragraphPdfDocumentReader.get();
       System.out.println(docs.size()+"hhhhhh");
        vectorStore.add(docs);
        
    }
}
