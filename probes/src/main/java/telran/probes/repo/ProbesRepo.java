package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.probes.model.SensorRangeDoc;

public interface ProbesRepo extends MongoRepository<SensorRangeDoc, Long> {

}
