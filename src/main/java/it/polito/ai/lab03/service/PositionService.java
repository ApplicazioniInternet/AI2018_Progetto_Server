package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.PositionRepository;
import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.repository.model.archive.ArchiveId;
import it.polito.ai.lab03.repository.model.position.Position;
import it.polito.ai.lab03.repository.model.position.PositionRepresentationCoordinates;
import it.polito.ai.lab03.repository.model.position.PositionRepresentationDownload;
import it.polito.ai.lab03.repository.model.position.PositionRepresentationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class PositionService {

    private PositionRepository positionRepository;

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Autowired
    public PositionService(PositionRepository pr)
    {
        this.positionRepository = pr;

    }

    public String insertPosition(Position position)
    {
        positionRepository.insert(position);
        return position.getId();
    }


    public List<Position> getAll()
    {
        return positionRepository.findAll();
    }

    public List<Position> getPositionsByUserId(String userId)
    {
        return positionRepository.findPositionsByUserId(userId);
    }

    private List<Position> getPositionsInArea(AreaRequest locationRequest)
    {
        if (locationRequest.getUserIds().isEmpty()) {
            return positionRepository
                    .findByLocationIsWithinAndTimestampBetweenAndOnSaleIsTrue(
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore()
                    );
        } else {
            return positionRepository
                    .findByUserIdInAndLocationIsWithinAndTimestampBetweenAndOnSaleIsTrue(
                            locationRequest.getUserIds(),
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore()
                    );
        }
    }

    public List<ArchiveId> getArchivesbyPositionsInArea(AreaRequest locationRequest) {
        return positionRepository
                .findArchivesIdbyPositionsInArea(
                        locationRequest.getArea(),
                        locationRequest.getTimestampAfter(),
                        locationRequest.getTimestampBefore()
                );
    }

    public int getArchivesCount(AreaRequest locationRequest) {
        return getArchivesbyPositionsInArea(locationRequest).size();
    }

    public void save(Position position) {
        positionRepository.save(position);
    }

    public PositionRepresentationDownload getRepresentations(AreaRequest locationRequest) {
        TreeSet<PositionRepresentationCoordinates>
                representationsByCoordinates = new TreeSet<>();
        TreeSet<PositionRepresentationTimestamp>
                representationsByTime = new TreeSet<>();

        List<Position> positionList = getPositionsInArea(locationRequest);

        for (int i = 0; i < positionList.size(); i++) {
            Position position = positionList.get(i);
            // aggiungo ai treeset di timestamp e coord -> ordino e filtro
            representationsByCoordinates.add(
                    new PositionRepresentationCoordinates(position));
            representationsByTime.add(
                    new PositionRepresentationTimestamp(position));
        }

        return new PositionRepresentationDownload(representationsByCoordinates, representationsByTime);

    }

    public int getPositionsCount(AreaRequest locationRequest) {
        if (locationRequest.getUserIds().isEmpty()) {
            return positionRepository
                    .countByLocationIsWithinAndTimestampBetweenAndOnSaleIsTrue(
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore()
                    );
        } else {
            return positionRepository
                    .countByUserIdInAndLocationIsWithinAndTimestampBetweenAndOnSaleIsTrue(
                            locationRequest.getUserIds(),
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore()
                    );
        }
    }
}
