package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.ArchiveRepository;
import it.polito.ai.lab03.repository.PositionRepresentationCoordinatesRepository;
import it.polito.ai.lab03.repository.PositionRepresentationTimestampRepository;
import it.polito.ai.lab03.repository.TransactionRepository;
import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.repository.model.Archive;
import it.polito.ai.lab03.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ArchiveService {

    private PositionService positionService;
    private ArchiveRepository archiveRepository;
    private TransactionRepository transactionRepository;
    private PositionValidator positionValidator;
    private UserDetailsServiceImpl userDetailsService;
    private PositionRepresentationCoordinatesRepository posRepresRepoCoord;
    private PositionRepresentationTimestampRepository posRepresRepoTime;

    @Autowired
    public ArchiveService(PositionService ps, ArchiveRepository ar,
                          PositionValidator pv, UserDetailsServiceImpl uds,
                          TransactionRepository tr,
                          PositionRepresentationCoordinatesRepository prrc,
                          PositionRepresentationTimestampRepository prrt) {
        this.archiveRepository = ar;
        this.positionService = ps;
        this.positionValidator = pv;
        this.transactionRepository = tr;
        this.userDetailsService = uds;
        this.posRepresRepoCoord = prrc;
        this.posRepresRepoTime = prrt;

    }

    public List<Archive> getAll() {
        return archiveRepository.findAll();
    }

    public List<Archive> getArchivesForUser(String user) {
        return archiveRepository.findArchivesByUserId(user);
    }

    public List<Archive> getArchivesBoughtCustomer(String user) {
        List<Archive> toBeReturned = new ArrayList<>();
        this.transactionRepository.findAllByBuyerId(user)
                .stream().map(Transaction::getArchiveBought)
                .forEach(archive -> {
                    toBeReturned.add(archive);
                    System.err.println(archive);
                });
        return toBeReturned;
    }

    public String insertArchive(Archive archive) {
        archiveRepository.insert(archive);
        return archive.getId();
    }

    public List<Archive> buyArchives(List<String> archivesId, String buyer) {
        List<Archive> archives = new ArrayList<>();
        archivesId.forEach( id -> archives.add(archiveRepository.findArchiveById(id)));

        // todo do transaction

        return archives;
    }

    @Transactional
    public StringResponse uploadArchive(User user, Positions positions) {
        TreeSet<PositionRepresentationCoordinates>
                representationsByCoordinates = new TreeSet<>(new ReprCoordComparator());
        TreeSet<PositionRepresentationTimestamp>
                representationsByTime = new TreeSet<>(new ReprTimeComparator());
        List<Position> positionsToAdd = new ArrayList<>();
        List<String> positionsId = new ArrayList<>();
        List<Position> ps;

        String id, username, userId;
        int count = 0, totCount = 0;
        boolean condition;

        username = user.getUsername();
        userId = user.getId();
        ps = positions.getPositions();
        Collections.sort(ps, (o1, o2) ->
            {
                if (o1.getTimestamp() - o2.getTimestamp() >= 0)
                    return 1;
                else
                    return -1;
            });
        for (int i = 0; i < ps.size(); i++) {
            Position position = ps.get(i);
            position.setUserId(username);
            // validazione posizione
            condition = positionValidator.isValidPosition(position, username);
            totCount++;
            /**
             * se valida aggiunta dell'id all'archivio
             * e della posizione vera (flag true)
             * e della sua rappresentazione (flag false)
             **/
            if (condition) {
                id = positionService.insertPosition(position);
                if (id != null) {
                    count++;
                    positionsId.add(id);
                    positionsToAdd.add(position);
                }
            }
        }

        // se c'è almeno una pos valida creo archivio se no exception
        if (positionsId.size() > 0) {
            Archive archive = new Archive(userId, positionsId);
            String archiveId = insertArchive(archive);
            for (int i = 0; i < positionsToAdd.size(); i++) {

                // creazione archivio e set di id archivio in posizione
                Position position = positionsToAdd.get(i);
                position.setArchiveId(archiveId);

                // aggiungo ai treeset di timestamp e coord -> ordino e filtro
                representationsByCoordinates.add(
                        new PositionRepresentationCoordinates(position));
                representationsByTime.add(
                        new PositionRepresentationTimestamp(position));
            }

            // itero i treeSet e aggiungo rappresentazioni di posizioni nel db
            Iterator<PositionRepresentationTimestamp> itrt = representationsByTime.iterator();
            while (itrt.hasNext()) {
                PositionRepresentationTimestamp position = itrt.next();
                posRepresRepoTime.insert(position);
            }

            Iterator<PositionRepresentationCoordinates> itrc = representationsByCoordinates.iterator();
            while (itrc.hasNext()) {
                PositionRepresentationCoordinates position = itrc.next();
                posRepresRepoCoord.insert(position);
            }

            return new StringResponse("Creato archivio con " + count + " posizioni valide su " + totCount);
        }

        // no pos added
        return null;
    }

    /*public List<Archive> buyArchivesInArea(AreaRequest locationRequest, String buyer) {
        List<Archive> archives = getArchivesInArea(locationRequest);
        System.err.println(locationRequest.toString());
        System.err.println("Archive in area: " + archives.size());
        //Divido la lista di posizioni da acquistare in liste divise per owner
        Map<String, List<Archive>> archivesListPerOwner = archives.stream()
                .collect(Collectors.groupingBy(Archive::getUserId, Collectors.toList()));
        //Per ogni utente diverso che possiede le archive che voglio comprare devo fare una transazione
        for (String owner : archivesListPerOwner.keySet()) {
            //Attualmente il prezzo penso sia sensato che sia costante * numero di posizioni acquistate
            double pricePaid = Constants.priceSingleArchive * archivesListPerOwner.get(owner).size();
            double revenueUser = Constants.percentageToUser * (Constants.priceSingleArchive * archivesListPerOwner.get(owner).size());
            //Costruzione della transazione (id autogenerato dal DB)
//            Transaction transaction = new Transaction(buyer, owner, archivesListPerOwner.get(owner), pricePaid, revenueUser, (System.currentTimeMillis() / 1000L));
//            transactionRepository.insert(transaction);
            //userDetailsService.updateByUsernameArchives(buyer, archivesListPerOwner.get(owner));
        }
        return archives;
    }*/
}