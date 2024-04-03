package telran.probes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class ServiceConfiguration {
	@Value("${app.account.provider.host}")
	String host;
	@Value("${app.account.provider.port}")
	int port;
	@Value("${app.account.provider.path}")
	String path;
	

	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
