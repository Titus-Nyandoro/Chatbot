package vua.inc.chatbot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;
import vua.inc.chatbot.service.DocumentsDownloaderService;
import vua.inc.chatbot.utils.Constants;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

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
            TikaDocumentReader documentReader2 = new TikaDocumentReader(getDocumentsFromLink());
            List<Document> documentsFromLinks = documentReader2.get();
            List<Document> splitLinkDocuments = textSplitter.apply(documentsFromLinks);
            vectorStore.add(splitLinkDocuments);
            
        };
    }
    @Bean
    public EmbeddingClient embeddingClient() {
        System.out.println("some key ====>" + key);
        // Can be any other EmbeddingClient implementation.
        return new OpenAiEmbeddingClient(new OpenAiApi(key));
    }

    @Bean
    public String getDocumentsFromLink() {
        return Constants.DOWNLOAD_LIST.stream()
                .map(url -> {
                    try {
                        return documentsDownloaderService.loadDocument(url);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to load document from " + url, e);
                    }
                })
                .collect(Collectors.joining("\n"));

        
    }

    
}