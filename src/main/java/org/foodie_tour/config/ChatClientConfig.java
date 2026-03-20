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

    @Value("${spring.ai.openai.chat.options.model}")
    private String GPT_OSS_120B;

    @Value("${spring.ai.openai.base-url}")
    private String CHAT_URL;

    @Value("${spring.ai.openai.api-key}")
    private String API_KEY_1;

    @Value("${spring.ai.openai.extend-key.key-2}")
    private String API_KEY_2;

    @Value("${spring.ai.openai.extend-key.key-3}")
    private String API_KEY_3;

    @Value("${spring.ai.openai.extend-key.key-4}")
    private String API_KEY_4;

    @Value("${spring.ai.openai.chat.options.temperature}")
    private double TEMPERATURE;

    @Bean
    public OpenAiApi apiKey1() {
        return OpenAiApi.builder()
                .apiKey(API_KEY_1)
                .baseUrl(CHAT_URL)
                .build();
    }

    @Bean
    public OpenAiApi apiKey2() {
        return OpenAiApi.builder()
                .apiKey(API_KEY_2)
                .baseUrl(CHAT_URL)
                .build();
    }

    @Bean
    public OpenAiApi apiKey3() {
        return OpenAiApi.builder()
                .apiKey(API_KEY_3)
                .baseUrl(CHAT_URL)
                .build();
    }

    @Bean
    public OpenAiApi apiKey4() {
        return OpenAiApi.builder()
                .apiKey(API_KEY_4)
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
    OpenAiChatModel modelKey1(@Qualifier(value = "apiKey1") OpenAiApi openAiApi) {
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
    OpenAiChatModel modelKey2(@Qualifier(value = "apiKey2") OpenAiApi openAiApi) {
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
    OpenAiChatModel modelKey3(@Qualifier(value = "apiKey3") OpenAiApi openAiApi) {
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
    OpenAiChatModel modelKey4(@Qualifier(value = "apiKey4") OpenAiApi openAiApi) {
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
    public ChatClient CLIENT_KEY_1(VectorStore vectorStore, @Qualifier(value = "modelKey1") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatClient CLIENT_KEY_2(VectorStore vectorStore, @Qualifier(value = "modelKey2") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatClient CLIENT_KEY_3(VectorStore vectorStore, @Qualifier(value = "modelKey3") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    public ChatClient CLIENT_KEY_4(VectorStore vectorStore, @Qualifier(value = "modelKey4") OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
