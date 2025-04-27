package core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"core", "repository", "service", "client", "config"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "model.entity")
@Slf4j
public class ScrapperApplication {

    public static void main(String[] args) {
        log.info("ScrapperApplication стартовал");
        SpringApplication.run(ScrapperApplication.class, args);
    }

}
