package com.github;

import com.github.bot.LinkTrackerBot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BotApplicationIntegrationTests {

	@Test
	void contextLoads(ApplicationContext ctx) {
		LinkTrackerBot bot = ctx.getBean(LinkTrackerBot.class);

		assertThat(bot).isNotNull();
		assertThat(bot.getBotUsername()).isNotEmpty();
	}

}
