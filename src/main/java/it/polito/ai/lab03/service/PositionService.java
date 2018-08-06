package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.PositionRepository;
import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.repository.model.archive.ArchiveLight;
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

    private List<Position> getPositionsInArea(AreaRequest locationRequest, String userId)
    {
        if (locationRequest.getUserIds().isEmpty()) {
            return positionRepository
                    .findByLocationIsWithinAndTimestampBetweenAndOnSaleIsTrueAndUserIdIsNot(
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore(),
                            userId
                    );
        } else {
            locationRequest.getUserIds().remove(userId);
            return positionRepository
                    .findByUserIdInAndLocationIsWithinAndTimestampBetweenAndOnSaleIsTrue(
                            locationRequest.getUserIds(),
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore()
                    );
        }
    }

    public List<ArchiveLight> getArchivesbyPositionsInArea(AreaRequest locationRequest, String userId) {
        return positionRepository
                .findArchivesIdbyPositionsInArea(
                        locationRequest.getArea(),
                        locationRequest.getTimestampAfter(),
                        locationRequest.getTimestampBefore(),
                        userId
                );
    }

    public int getArchivesCount(AreaRequest locationRequest, String userId) {
        return getArchivesbyPositionsInArea(locationRequest, userId).size();
    }

    public void save(Position position) {
        positionRepository.save(position);
    }

    public PositionRepresentationDownload getRepresentations(AreaRequest locationRequest, String userId) {
        TreeSet<PositionRepresentationCoordinates>
                representationsByCoordinates = new TreeSet<>();
        TreeSet<PositionRepresentationTimestamp>
                representationsByTime = new TreeSet<>();

        List<Position> positionList = getPositionsInArea(locationRequest, userId);

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

    public int getPositionsCount(AreaRequest locationRequest, String userId) {
        if (locationRequest.getUserIds().isEmpty()) {
            return positionRepository
                    .countByLocationIsWithinAndTimestampBetweenAndOnSaleIsTrueAndUserIdIsNot(
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore(),
                            userId
                    );
        } else {
            locationRequest.getUserIds().remove(userId);
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
