package inc.alc.beercounter.dao.impl;

import inc.alc.beercounter.dao.CountRepository;
import inc.alc.beercounter.domain.Count;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface CountRepositoryMongoDb extends MongoRepository<Count, String>, CountRepository {

}
