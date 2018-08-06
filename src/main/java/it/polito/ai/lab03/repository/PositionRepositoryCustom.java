package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.archive.ArchiveLight;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PositionRepositoryCustom {

    List<ArchiveLight> findArchivesIdbyPositionsInArea(@NonNull GeoJsonPolygon area,
                                                       @NonNull long timestampStart,
                                                       @NonNull long timestampEnd,
                                                       @NonNull String userId);
}
