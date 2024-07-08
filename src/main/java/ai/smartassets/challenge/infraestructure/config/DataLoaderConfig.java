package ai.smartassets.challenge.infraestructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Configuration @Slf4j
public class DataLoaderConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    CommandLineRunner loadData() {
        return args -> {
            log.info("Starting data loading process");
            ObjectMapper mapper = new ObjectMapper();
            List<String> collections = List.of("brands", "campaigns", "creatives");
            for (String collection : collections) {
                try {
                    log.info("Loading data for collection: {}", collection);
                    InputStream inputStream = new ClassPathResource("db/" + collection + ".json").getInputStream();
                    List<Map<String, Object>> data = mapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});
                    mongoTemplate.getCollection(collection).insertMany(
                            data.stream()
                                    .map(mongoTemplate.getConverter()::convertToMongoType)
                                    .map(obj -> (Document) obj)
                                    .toList());
                    log.info("Successfully loaded data for collection: {}", collection);
                } catch (Exception e) {
                    log.error("Error loading data for collection: {}", collection, e);
                }
            }
            log.info("Data loading process completed");
        };
    }
}