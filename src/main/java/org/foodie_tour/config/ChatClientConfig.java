package org.foodie_tour.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ChatClientConfig {

    @Value("${spring.ai.openai.chat.options.model.custom-1}")
    private String LLAMA3_8B;

    @Value("${spring.ai.openai.chat.options.model.custom-2}")
    private String LLAMA3_70B;

    @Value("${spring.ai.openai.chat.options.model.custom-3}")
    private String GPT_OSS_120B;

    @Value("${spring.ai.openai.chat.options.model.custom-4}")
    private String GPT_OSS_20B;

    @Value("${spring.ai.openai.chat.options.model.custom-5}")
    private String LLAMA4_17B;

    @Value("${spring.ai.openai.base-url}")
    private String CHAT_URL;

    @Value("${spring.ai.openai.api-key}")
    private String API_KEY;

    @Value("${spring.ai.openai.chat.options.temperature}")
    private double TEMPERATURE;

    @Bean
    public OpenAiApi openAiApi() {
        return OpenAiApi.builder()
                .apiKey(API_KEY)
                .baseUrl(CHAT_URL)
                .build();
    }

    @Bean
    public ChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(20)
                .build();
    }

    @Bean
    public VectorStore vectorStore(DataSource dataSource, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(new JdbcTemplate(dataSource), embeddingModel)
                .dimensions(1536)
                .vectorTableName("vectors")
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .initializeSchema(true)
                .build();
    }

    @Bean
    OpenAiChatModel llama38bModel(OpenAiApi openAiApi) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(LLAMA3_8B)
                .temperature(TEMPERATURE)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();
    }

    @Bean
    OpenAiChatModel llama370bModel(OpenAiApi openAiApi) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(LLAMA3_70B)
                .temperature(TEMPERATURE)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();
    }

    @Bean
    OpenAiChatModel gptOss120bModel(OpenAiApi openAiApi) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(GPT_OSS_120B)
                .temperature(TEMPERATURE)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();
    }

    @Bean
    OpenAiChatModel gptOss20bModel(OpenAiApi openAiApi) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(GPT_OSS_20B)
                .temperature(TEMPERATURE)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();
    }

    @Bean
    OpenAiChatModel llama417bModel(OpenAiApi openAiApi) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(LLAMA4_17B)
                .temperature(TEMPERATURE)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();
    }

    @Bean
    public ChatClient LLAMA3_8B(VectorStore vectorStore, @Qualifier(value = "llama38bModel") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatClient LLAMA3_70B(VectorStore vectorStore, @Qualifier(value = "llama370bModel") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatClient GPT_OSS_120B(VectorStore vectorStore, @Qualifier(value = "gptOss120bModel") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatClient GPT_OSS_20B(VectorStore vectorStore, @Qualifier(value = "gptOss20bModel") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatClient LLAMA4_17B(VectorStore vectorStore, @Qualifier(value = "llama417bModel") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
