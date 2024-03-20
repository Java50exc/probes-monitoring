package telran.probes;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import telran.probes.dto.ProbeData;
import telran.probes.service.AvgReducerService;

@SpringBootApplication
@RequiredArgsConstructor
public class AvgReducerAppl {
	final StreamBridge streamBridge;
	final AvgReducerService reducerService;
	
	String producerBindingName = "avgReducerProducer-out-0";
	
	public static void main(String[] args) {
		SpringApplication.run(AvgReducerAppl.class, args);
	}
	
	@Bean
	Consumer<ProbeData> avgReducerConsumer() {
		return probeData -> probeDataReducing(probeData);
	}

	private void probeDataReducing(ProbeData probeData) {
		Double avgValue = reducerService.getAvgValue(probeData);
		
		if (avgValue != null) {
			ProbeData avgData = new ProbeData(probeData.id(), avgValue, System.currentTimeMillis());
			streamBridge.send(producerBindingName, avgData);
		}
	}

}
