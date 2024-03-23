package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.probes.model.SensorEmailsDoc;

public interface EmailsRepo extends MongoRepository<SensorEmailsDoc, Long> {

}
