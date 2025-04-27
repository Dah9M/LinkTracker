package core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"core", "repository", "service", "command", "bot", "controller"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "model.entity")
public class BotApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotApplication.class, args);
		log.info("Приложение успешно запущено.");
	}

}

