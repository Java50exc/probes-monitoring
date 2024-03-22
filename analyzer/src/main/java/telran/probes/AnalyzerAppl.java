package telran.probes;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import telran.probes.dto.*;
import telran.probes.service.RangeProviderClientService;

@SpringBootApplication
@RequiredArgsConstructor
public class AnalyzerAppl {
	final StreamBridge streamBridge;
	final RangeProviderClientService clientService;
	
	@Value("${app.analyzer.producer.binding.name}")
	String producerBindingName;

	public static void main(String[] args) {
		SpringApplication.run(AnalyzerAppl.class, args);
	}


	@Bean
	Consumer<ProbeData> analyzerConsumer() {
		return probeData -> probeDataAnalyzing(probeData);
	}

	private void probeDataAnalyzing(ProbeData probeData) {
		Range range = clientService.getRange(probeData.id());
		double probeValue = probeData.value();

		double deviationValue = probeValue < range.minValue() ? probeValue - range.minValue()
				: probeValue > range.maxValue() ? probeValue - range.maxValue() : 0;

		if (deviationValue != 0) {
			DeviationData deviation = new DeviationData(probeData.id(), deviationValue, probeValue,
					probeData.timestamp());
			streamBridge.send(producerBindingName, deviation);
		}
	}
	
	@Bean
	Consumer<SensorUpdateData> updateRangeConsumer() {
		return updateData -> updateProcessing(updateData);
	}

	private void updateProcessing(SensorUpdateData updateData) {
		if (updateData.range() != null) {
			clientService.updateCache(updateData.id(), updateData.range());
		}
	}

}
