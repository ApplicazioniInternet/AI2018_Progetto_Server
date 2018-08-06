package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.position.Position;
import it.polito.ai.lab03.repository.model.User;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.List;

@SuppressWarnings("ALL")
public interface PositionRepository extends MongoRepository<Position, User>, PositionRepositoryCustom {
    List<Position> findAll();
    Position insert(@NonNull Position position);
    Position findPositionsById(@NonNull String id);
    List<Position> findPositionsByUserId(@NonNull String userId);

    int countByLocationIsWithinAndTimestampBetweenAndOnSaleIsTrueAndUserIdIsNot(
            @NonNull GeoJsonPolygon area, @NonNull double timestamp1,
            @NonNull double timestamp2, @NonNull String userId);
    int countByUserIdInAndLocationIsWithinAndTimestampBetweenAndOnSaleIsTrue(
            @NonNull List<String> userId, @NonNull GeoJsonPolygon area,
            @NonNull double timestamp1, @NonNull double timestamp2);

    List<Position> findByLocationIsWithinAndTimestampBetweenAndOnSaleIsTrueAndUserIdIsNot(
            @NonNull GeoJsonPolygon area, @NonNull long timestampStart,
            @NonNull long timestampEnd, @NonNull String userId);
    List<Position> findByUserIdInAndLocationIsWithinAndTimestampBetweenAndOnSaleIsTrue(
            @NonNull List<String> userId, @NonNull GeoJsonPolygon area, @NonNull long timestampStart,
            @NonNull long timestampEnd);
}
