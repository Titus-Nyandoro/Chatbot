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
import org.springframework.core.io.Resource;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;

import java.util.List;

@Configuration
@Slf4j
public class AiModelConfig {
    @Value("classpath:/vua-FAQ-BUILDUP.pdf")
    private Resource faq;
    @Value("classpath:/7.-Personal-Financial-Management-Kenya.pdf")
    private Resource finance;

    @Value("${spring.ai.openai.api-key}")
    private String key;
    @Bean
    ApplicationRunner loadInfo(VectorStore vectorStore){
        return args -> {

            PagePdfDocumentReader pagePdfDocumentReaderFAQ = new PagePdfDocumentReader(faq);
            List<Document> faqDocs = pagePdfDocumentReaderFAQ.get();
           vectorStore.add(faqDocs);
           PagePdfDocumentReader pagePdfDocumentReaderFinance = new PagePdfDocumentReader(finance);
            List<Document> financeDocs = pagePdfDocumentReaderFinance.get();
           vectorStore.add(financeDocs);
            
        };
    }
    @Bean
    public EmbeddingClient embeddingClient() {
        System.out.println("some key ====>" + key);
        // Can be any other EmbeddingClient implementation.
        return new OpenAiEmbeddingClient(new OpenAiApi(key));
    }
  
}
