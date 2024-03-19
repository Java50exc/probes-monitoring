package telran.probes.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import telran.probes.dto.ProbeData;

@Document(collection = "avg_values")
@ToString
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class ProbeDataDoc {
	long sensorId;
	LocalDateTime timestamp;
	Double value;
	
	public ProbeDataDoc(ProbeData probeData) {
		Instant instant = Instant.ofEpochMilli(probeData.timestamp());
		timestamp = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		value = probeData.value();
		sensorId = probeData.id();
	}

}
