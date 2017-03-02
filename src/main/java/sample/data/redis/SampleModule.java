package sample.data.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class SampleModule {
    @Bean
    public ObjectMapper objectMapper() {
        final String DATE_FORMAT = "yyyy-MM-dd '00:00:00Z'";
        final DateTimeFormatter DATE_FORMATTER =
                DateTimeFormatter.ofPattern(DATE_FORMAT);
        final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss'Z'";
        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer(DATE_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer(DATE_FORMATTER));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));


        return Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .modules(javaTimeModule)
                .build();
    }

    @Bean
    @Primary
    public ElasticsearchTemplate elasticsearchTemplate(ObjectMapper objectMapper, Client client){
        return  new ElasticsearchTemplate(client, new EntityMapper() {
            @Override
            public String mapToString(Object object) throws IOException {
                return objectMapper.writeValueAsString(object);
            }

            @Override
            public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
                return objectMapper.readValue(source, clazz);
            }
        });
    }

    @Bean
    public RedisTemplate<?, ?> getRedisTemplate(ObjectMapper objectMapper, RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
