package vua.inc.chatbot.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(
                            new ExtractedTextFormatter.Builder()
                                    .build())
                    .build();

            // reading the resource
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            // Defining the pattern to match all files in the doc folder
            String locationPattern = "classpath*:docs/*";
            // get all resources matching the pattern meaning they're in the docs folder
            Resource [] resources = resolver.getResources(locationPattern);
            // Iterate through each resource
            for (Resource resource : resources) {
                System.out.println(resource.getFile());
                var pdfReader = new PagePdfDocumentReader(resource, config);
                var textSplitter = new TokenTextSplitter();
                vectorStore.accept(textSplitter.apply(pdfReader.get()));
            }

//            PagePdfDocumentReader pagePdfDocumentReaderFAQ = new PagePdfDocumentReader(faq);
//            List<Document> faqDocs = pagePdfDocumentReaderFAQ.get();
//           vectorStore.add(faqDocs);
//           PagePdfDocumentReader pagePdfDocumentReaderFinance = new PagePdfDocumentReader(finance);
//            List<Document> financeDocs = pagePdfDocumentReaderFinance.get();
//           vectorStore.add(financeDocs);
//
        };
    }
    @Bean
    public EmbeddingClient embeddingClient() {
        System.out.println("some key ====>" + key);
        // Can be any other EmbeddingClient implementation.
        return new OpenAiEmbeddingClient(new OpenAiApi(key));
    }

}
