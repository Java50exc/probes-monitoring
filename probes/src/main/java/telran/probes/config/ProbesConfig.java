package telran.probes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class ProbesConfig {
	@Value("${app.probes.initial.random.sensor.id}")
	private long initialSensorId;
	@Value("${app.probes.initial.random.min.range}")
	private double minValue;
	@Value("${app.probes.initial.random.max.range}")
	private double maxValue;
	@Value("${app.probes.random.deviation.probability}")
	private Double deviationProb;
	@Value("${app.probes.random.negative.probability}")
	private Double lessProb;
	@Value("${app.probes.random.number.sensors}")
	private int nSensors;

}
