package vua.inc.chatbot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import vua.inc.chatbot.service.DocumentsDownloaderService;
import vua.inc.chatbot.utils.Constants;

import java.util.ArrayList;
import java.util.List;


@Configuration
@Slf4j
public class AiModelConfig {
    @Value("classpath:/info.txt")
    private Resource info;

    @Value("${spring.ai.openai.api-key}")
    private String key;

    private final DocumentsDownloaderService documentsDownloaderService;
    public AiModelConfig(DocumentsDownloaderService docService){
        this.documentsDownloaderService = docService;
    }
    @Bean
    ApplicationRunner loadInfo(VectorStore vectorStore){
        return args -> {
            TikaDocumentReader documentReader = new TikaDocumentReader(info);
            List<Document> documents = documentReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);

            vectorStore.add(splitDocuments);
        //    add resources from links
            documentsDownloaderService.getDocumentsFromLink();
            // Thread.sleep(3000);
           PagePdfDocumentReader paragraphPdfDocumentReader = new PagePdfDocumentReader("src/main/resources/downloaded.pdf");
            List<Document> docs = paragraphPdfDocumentReader.get();
        //    List<Document> docs = documentsDownloaderService.getDocumentsFromLink();
           System.out.println(docs.size()+"hhhhhh");
            vectorStore.add(docs);
            
        };
    }
    @Bean
    public EmbeddingClient embeddingClient() {
        System.out.println("some key ====>" + key);
        // Can be any other EmbeddingClient implementation.
        return new OpenAiEmbeddingClient(new OpenAiApi(key));
    }

    
  

    
}
