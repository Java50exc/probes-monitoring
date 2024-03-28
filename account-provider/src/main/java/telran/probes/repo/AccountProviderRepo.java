package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.probes.model.AccountDoc;

public interface AccountProviderRepo extends MongoRepository<AccountDoc, String> {

}
