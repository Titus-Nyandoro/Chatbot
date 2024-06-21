package vua.inc.chatbot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@Slf4j
public class AiModelConfig {

    @Value("${spring.ai.openai.api-key}")
    private String key;

    @Bean
    public EmbeddingClient embeddingClient() {
        System.out.println("some key ====>" + key);
        // Can be any other EmbeddingClient implementation.
        return new OpenAiEmbeddingClient(new OpenAiApi(key));
    }

}
