package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.Position;
import it.polito.ai.lab03.repository.model.User;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.List;

@SuppressWarnings("ALL")
public interface PositionRepository extends MongoRepository<Position, User> {
    Position findPositionsById(@NonNull String id);
    List<Position> findPositionsByUserId(@NonNull String userId);
    List<Position> findPositionsByArchiveId(@NonNull String archiveId);
    List<Position> findPositionsByLatitudeAndLongitude(@NonNull double latitude, @NonNull double longitude);
    List<Position> findPositionsByTimestampAfter(@NonNull long timestamp);
    List<Position> findPositionsByTimestampBefore(@NonNull long timestamp);
    List<Position> findPositionsByTimestampBeforeAndTimestampAfter(@NonNull long t1, @NonNull long t2);
    Position insert(@NonNull Position position);

    int countByLocationIsWithinAndTimestampBetween(@NonNull GeoJsonPolygon area, @NonNull double timestamp1, @NonNull double timestamp2);

    List<Position> findByLocationIsWithinAndTimestampBetween(@NonNull GeoJsonPolygon area, @NonNull long timestampStart, @NonNull long timestampEnd);
    List<Position> findByUserIdInAndLocationIsWithinAndTimestampBetween(
            @NonNull List<String> userId, @NonNull GeoJsonPolygon area, @NonNull long timestampStart,
            @NonNull long timestampEnd);
    Position deletePositionById(@NonNull String positionId);
    List<Position> deletePositionByUserId(@NonNull String userId);
}
