package it.polito.ai.lab03.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import it.polito.ai.lab03.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    public @Bean
    MongoClient mongo() {
        MongoClientURI connectionStr = new MongoClientURI(Constants.DATABASE_URI);
        return new MongoClient(connectionStr);
    }

    public @Bean
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), Constants.DATABASE_NAME);
    }
}
