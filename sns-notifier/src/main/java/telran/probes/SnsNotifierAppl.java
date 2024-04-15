package telran.probes;

import java.time.*;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.DeviationData;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SnsNotifierAppl {
	@Value("${app.mail.notifier.subject}")
	private String header;
	@Value("${app.mail.notifier.text}")
	private String content;
	@Value("${app.mail.notifier.arn}")
	private String topicArnStatic;
	
	private static AmazonSNS client;
	
	
	public static void main(String[] args) {
		SpringApplication.run(SnsNotifierAppl.class, args);
		client = AmazonSNSClient.builder().withRegion(Regions.US_EAST_1).build();
	}
	
	
	@Bean
	Consumer<DeviationData> snsNotifierConsumer() {
		return deviation -> sendingMail(deviation);
	}


	private void sendingMail(DeviationData deviation) {
		String topicArn = topicArnStatic + ":sensor-" + deviation.id();
		String msgContent = String.format(content, deviation.id(), deviation.value(), deviation.deviation(), getDateTime(deviation.timestamp()));
		String subject = header + deviation.id();
		client.publish(topicArn, msgContent, subject);
		log.debug("sent message {} with header {} to topic {}", msgContent, subject, topicArn);
	}
	
	
	private LocalDateTime getDateTime(long timestamp) {
		return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	


}
