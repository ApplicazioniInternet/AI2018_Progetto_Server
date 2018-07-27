package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.PositionRepresentationCoordinates;
import it.polito.ai.lab03.repository.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

@SuppressWarnings("ALL")
public interface PositionRepresentationCoordinatesRepository
        extends MongoRepository<PositionRepresentationCoordinates, User> {
    PositionRepresentationCoordinates insert(@NonNull PositionRepresentationCoordinates position);
}
