package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import telran.probes.dto.Range;

@Document(collection = "ranges")
@Getter
@AllArgsConstructor
public class RangeDoc {
	@Id
	long id;
	Range range;
}
