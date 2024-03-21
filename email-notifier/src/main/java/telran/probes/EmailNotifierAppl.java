package telran.probes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.DeviationData;
import telran.probes.dto.SensorUpdateData;
import telran.probes.service.EmailsProviderClientService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class EmailNotifierAppl {
	@Value("${app.mail.notifier.subject:deviation of sensor }")
	private String subject;
	final EmailsProviderClientService clientService;
	final JavaMailSender mailSender;
	
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
	
	@Bean
	Consumer<DeviationData> emailNotifierConsumer() {
		return deviation -> sendingMail(deviation);
	}


	private void sendingMail(DeviationData deviation) {
		log.debug("received deviation data: {}", deviation);
		SimpleMailMessage smm = new SimpleMailMessage();
		Long sensorId = deviation.id();
		String[] emails = clientService.getMails(sensorId);
		log.debug("received mail addresses are: {}", Arrays.toString(emails));
		
		smm.setTo(emails);
		smm.setText(getText(deviation));
		smm.setSubject(getSubject(sensorId));
		mailSender.send(smm);
	}


	private String getSubject(Long sensorId) {
		String result = subject + sensorId;
		log.debug("subject: {}", subject);
		return result;
	}


	private String getText(DeviationData deviation) {
		String text = String.format("sensor %d has value %f with deviation %f at %s",
				deviation.id(), deviation.value(), deviation.deviation(), getDateTime(deviation.timestamp()));
		return text;
	}


	private LocalDateTime getDateTime(long timestamp) {
		return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}
