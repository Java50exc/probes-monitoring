package telran.probes.repo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import telran.probes.model.ProbeDataDoc;

public interface AvgPopulatorRepo extends MongoRepository<ProbeDataDoc, ObjectId> {
	List<ProbeDataDoc> findBySensorId(long id);

}
