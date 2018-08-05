package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.archive.ArchiveId;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PositionRepositoryCustom {

    List<ArchiveId> findArchivesIdbyPositionsInArea(@NonNull GeoJsonPolygon area,
                                                      @NonNull long timestampStart,
                                                      @NonNull long timestampEnd);
}
