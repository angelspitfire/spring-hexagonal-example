package ai.smartassets.challenge.infraestructure.config;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!docker")
@Configuration
public class CustomEmbeddedMongoAutoConfiguration extends EmbeddedMongoAutoConfiguration {
}
