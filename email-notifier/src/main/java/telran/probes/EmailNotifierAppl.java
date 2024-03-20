package telran.probes;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import telran.probes.dto.SensorUpdateData;
import telran.probes.service.EmailsProviderClientService;

@SpringBootApplication
@RequiredArgsConstructor
public class EmailNotifierAppl {
	final EmailsProviderClientService clientService;
	
	public static void main(String[] args) {
		SpringApplication.run(EmailNotifierAppl.class, args);
	}
	
	
	@Bean
	Consumer<SensorUpdateData> updateEmailsConsumer() {
		return updateData -> updateProcessing(updateData);
	}

	private void updateProcessing(SensorUpdateData updateData) {
		if (updateData.emails() != null) {
			clientService.updateCache(updateData.id(), updateData.emails());
		}
	}

}
