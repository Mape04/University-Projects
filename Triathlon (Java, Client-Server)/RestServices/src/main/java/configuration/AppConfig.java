package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Properties;

@Configuration
public class AppConfig {

    @Bean
    @Qualifier("dbProperties")
    public Properties dbProperties() {
        Properties properties = new Properties();
        properties.setProperty("jdbc.url", "jdbc:sqlite:C:\\Users\\mazilu\\Documents\\MPP\\mpp-proiect-java-MaziluPaul\\Triathlon\\Database_source\\Triathlon.db");
        properties.setProperty("jdbc.driver", "org.sqlite.JDBC");
        properties.setProperty("server.port", "55556");
//        properties.setProperty("db.username", "null");
//        properties.setProperty("db.password", "null");
        // add other properties as needed
        return properties;
    }
}
