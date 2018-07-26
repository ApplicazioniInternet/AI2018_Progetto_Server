package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.PositionRepository;
import it.polito.ai.lab03.repository.TransactionRepository;
import it.polito.ai.lab03.repository.model.AreaRequest;
import it.polito.ai.lab03.repository.model.Position;
import it.polito.ai.lab03.repository.model.Transaction;
import it.polito.ai.lab03.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository pr) {
        this.positionRepository = pr;

    }

    public String insertPosition(Position position) {
        positionRepository.insert(position);
        return position.getId();
    }


    public List<Position> getAll() {
        return positionRepository.findAll();
    }

    public List<Position> getPositionsForUser(String user) {
        return positionRepository.findPositionsByUserId(user);
    }

    private List<Position> getPositionsInArea(AreaRequest locationRequest) {
        return positionRepository
                .findByLocationIsWithinAndTimestampBetween(
                        locationRequest.getArea(),
                        locationRequest.getTimestampAfter(),
                        locationRequest.getTimestampBefore()
                );
    }

    public int getNumberPositionsInArea(AreaRequest locationRequest) {
        return positionRepository
                .countByLocationIsWithinAndTimestampBetween(
                        locationRequest.getArea(),
                        locationRequest.getTimestampAfter(),
                        locationRequest.getTimestampBefore()
                );
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
