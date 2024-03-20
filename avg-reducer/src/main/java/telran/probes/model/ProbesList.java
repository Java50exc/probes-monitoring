package telran.probes.model;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;

@RedisHash
@RequiredArgsConstructor
@Getter
public class ProbesList {
	@Id
	@NonNull Long sensorId;
	List<Double> value = new ArrayList<>();
	
	
}
