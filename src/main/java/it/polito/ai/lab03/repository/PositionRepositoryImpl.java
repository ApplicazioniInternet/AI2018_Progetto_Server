package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.ArchiveId;
import it.polito.ai.lab03.repository.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;


import java.util.List;

@Repository
public class PositionRepositoryImpl implements PositionRepositoryCustom {


    private final MongoTemplate mongoTemplate;

    @Autowired
    public PositionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<ArchiveId> findArchivesIdbyPositionsInArea(@NonNull GeoJsonPolygon area,
                                                 @NonNull long timestampStart,
                                                 @NonNull long timestampEnd) {

        Aggregation agg = newAggregation(
                match(Criteria.where("location").within(area)
                        .and("timestamp").gte(timestampStart).lte(timestampEnd)),
                group("archiveId"),
                project("_id").and("archiveId").previousOperation()
        );

        //Convert the aggregation result into a List
        AggregationResults<ArchiveId> groupResults
                = mongoTemplate.aggregate(agg, Position.class, ArchiveId.class);

        List<ArchiveId> result = groupResults.getMappedResults();

        return result;
    }
}
