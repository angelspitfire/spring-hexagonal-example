package ai.smartassets.challenge;

import ai.smartassets.challenge.domain.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class MongoDbSpringIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testMongoDb() {

        Brand document = new Brand("1", "Test Brand", "Description");
        document.setName("Test Name");

        mongoTemplate.save(document);

        long count = mongoTemplate.count(Query.query(Criteria.where("name").is("Test Name")), Brand.class);
        assertEquals(1, count);
    }
}
