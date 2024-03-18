package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.probes.model.RangeDoc;

public interface SensorRangeProviderRepo extends MongoRepository<RangeDoc, Long> {

}
