package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.PositionRepository;
import it.polito.ai.lab03.repository.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
                    .findByLocationIsWithinAndTimestampBetween(
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore()
                    );
        } else {
            return positionRepository
                    .findByUserIdInAndLocationIsWithinAndTimestampBetween(
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

    public List<Position> getPositionsByArchiveId(String id) {
        return positionRepository.findPositionsByArchiveId(id);
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
                    .countByLocationIsWithinAndTimestampBetween(
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore()
                    );
        } else {
            return positionRepository
                    .countByUserIdInAndLocationIsWithinAndTimestampBetween(
                            locationRequest.getUserIds(),
                            locationRequest.getArea(),
                            locationRequest.getTimestampAfter(),
                            locationRequest.getTimestampBefore()
                    );
        }
    }

    /*public List<Position> buyPositionsInArea(AreaRequest locationRequest, String buyer) {
        List<Position> positions = getPositionsInArea(locationRequest);
        System.err.println(locationRequest.toString());
        System.err.println("Positions in area: " + positions.size());
        //Divido la lista di posizioni da acquistare in liste divise per owner
        Map<String, List<Position>> positionsListPerOwner = positions.stream()
                .collect(Collectors.groupingBy(Position::getUserId, Collectors.toList()));
        //Per ogni utente diverso che possiede le position che voglio comprare devo fare una transazione
        for (String owner : positionsListPerOwner.keySet()) {
            //Attualmente il prezzo penso sia sensato che sia costante * numero di posizioni acquistate
            double pricePaid = Constants.priceSinglePosition * positionsListPerOwner.get(owner).size();
            double revenueUser = Constants.percentageToUser * (Constants.priceSinglePosition * positionsListPerOwner.get(owner).size());
            //Costruzione della transazione (id autogenerato dal DB)
            Transaction transaction = new Transaction(buyer, owner, positionsListPerOwner.get(owner), pricePaid, revenueUser, (System.currentTimeMillis() / 1000L));
            transactionRepository.insert(transaction);
            //userDetailsService.updateByUsernamePositions(buyer, positionsListPerOwner.get(owner));
        }
        return positions;
    }*/
}
