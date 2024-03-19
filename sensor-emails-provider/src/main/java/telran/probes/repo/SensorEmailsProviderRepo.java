package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.probes.model.SensorEmailsDoc;

public interface SensorEmailsProviderRepo extends MongoRepository<SensorEmailsDoc, Long> {

}
