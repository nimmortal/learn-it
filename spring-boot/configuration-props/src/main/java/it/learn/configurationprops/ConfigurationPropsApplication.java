package it.learn.configurationprops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ConfigurationPropsApplication {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationPropsApplication.class);

    private final ApplicationProperties properties;

    @Autowired
    public ConfigurationPropsApplication(ApplicationProperties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationPropsApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doAfterStartup() {
        log.info(properties.toString());
    }
}
