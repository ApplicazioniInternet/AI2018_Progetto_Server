package it.polito.ai.lab03.repository;

import it.polito.ai.lab03.repository.model.archive.ArchiveLight;
import it.polito.ai.lab03.repository.model.position.Position;
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
    private final TransactionRepository transactionRepository;

    @Autowired
    public PositionRepositoryImpl(MongoTemplate mongoTemplate,
                                  TransactionRepository tr) {
        this.mongoTemplate = mongoTemplate;
        this.transactionRepository = tr;
    }

    public List<ArchiveLight> findArchivesIdbyPositionsInArea(@NonNull GeoJsonPolygon area,
                                                              @NonNull long timestampStart,
                                                              @NonNull long timestampEnd,
                                                              @NonNull String userId) {

        Aggregation agg = newAggregation(
                match(Criteria.where("location").within(area)
                        .and("onSale").is(true).and("userId").ne(userId)
                        .and("timestamp").gte(timestampStart).lte(timestampEnd)),
                group("archiveId"),
                project("_id").and("archiveId").previousOperation()
        );

        //Convert the aggregation result into a List
        AggregationResults<ArchiveLight> groupResults
                = mongoTemplate.aggregate(agg, Position.class, ArchiveLight.class);

        List<ArchiveLight> result = groupResults.getMappedResults();

        result.forEach(
                archiveId -> {
                    if (transactionRepository.
                            findByBuyerIdAndArchivesBoughtArchiveListContains(userId, archiveId).size() > 0)
                        archiveId.setBought(true);
                }
        );

        return result;
    }
}
