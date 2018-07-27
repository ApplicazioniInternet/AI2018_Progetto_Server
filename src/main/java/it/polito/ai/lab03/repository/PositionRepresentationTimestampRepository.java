package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.PositionRepresentationTimestamp;
import it.polito.ai.lab03.repository.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

@SuppressWarnings("ALL")
public interface PositionRepresentationTimestampRepository
        extends MongoRepository<PositionRepresentationTimestamp, User> {
    PositionRepresentationTimestamp insert(@NonNull PositionRepresentationTimestamp position);

}
