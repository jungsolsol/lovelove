package webapp.lovelove;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude={MultipartAutoConfiguration.class})
public class LoveloveApplication  {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "optional:classpath:application-oauth.yml,"
			+ "optional:/usr/local/myapp/application-oauth.yml";
	public static void main(String[] args) {

		new SpringApplicationBuilder(LoveloveApplication.class).properties(APPLICATION_LOCATIONS)
				.run(args);
	}

}
