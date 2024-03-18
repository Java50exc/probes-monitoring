package telran.probes;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import telran.probes.dto.DeviationData;
import telran.probes.dto.ProbeData;
import telran.probes.dto.Range;
import telran.probes.service.RangeProviderClientService;

@SpringBootApplication
@RequiredArgsConstructor
public class AnalyzerAppl {
	final StreamBridge streamBridge;
	final RangeProviderClientService clientService;

	String producerBindingName = "analyzerProducer-out-0";

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

}
