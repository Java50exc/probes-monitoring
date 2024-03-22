package telran.probes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class ServiceConfiguration {
	@Value("${app.emails.provider.host}")
	String host;
	@Value("${app.emails.provider.port}")
	int port;
	@Value("${app.emails.provider.path}")
	String path;
	@Value("${app.email.notifier.default.mail}")
	String defaultEmail;
	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
