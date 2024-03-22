package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import telran.probes.dto.Range;

@Document("ranges")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SensorRangeDoc {
	@Id
	Long id;
	Range range;
	

}
