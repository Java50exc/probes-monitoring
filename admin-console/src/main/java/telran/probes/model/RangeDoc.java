package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.probes.dto.Range;
import telran.probes.dto.SensorRange;

@Document(collection = "ranges")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RangeDoc {
	@Id
	long id;
	Range range;
	
	public SensorRange build() {
		return new SensorRange(id, range);	
	}
	
	public RangeDoc(SensorRange sensorRange) {
		id = sensorRange.id();
		range = sensorRange.range();
	}
}
