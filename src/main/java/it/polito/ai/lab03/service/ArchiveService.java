package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.ArchiveRepository;
import it.polito.ai.lab03.repository.TransactionRepository;
import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.repository.model.archive.Archive;
import it.polito.ai.lab03.repository.model.archive.ArchiveDownload;
import it.polito.ai.lab03.repository.model.position.Position;
import it.polito.ai.lab03.repository.model.position.PositionDownload;
import it.polito.ai.lab03.repository.model.position.Positions;
import it.polito.ai.lab03.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ArchiveService {

    private PositionService positionService;
    private ArchiveRepository archiveRepository;
    private TransactionRepository transactionRepository;
    private PositionValidator positionValidator;

    @Autowired
    public ArchiveService(PositionService ps,
                          ArchiveRepository ar,
                          PositionValidator pv,
                          TransactionRepository tr) {
        this.archiveRepository = ar;
        this.positionService = ps;
        this.positionValidator = pv;
        this.transactionRepository = tr;
    }

    public List<Archive> getAll() {
        return archiveRepository.findAll();
    }

    public List<Archive> getArchivesForUser(String user) {
        return archiveRepository.findArchivesByUserIdAndOnSale(user, true);
    }

    public List<Archive> getArchivesBoughtCustomer(String user) {
        List<Archive> toBeReturned = new ArrayList<>();
        this.transactionRepository.findAllByBuyerId(user)
                .stream().map(Transaction::getArchivesBought);
        return toBeReturned;
    }

    private String insertArchive(Archive archive) {
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
        List<Position> ps;

        String id, username, userId;
        int count = 0, totCount = 0;
        boolean condition;

        username = user.getUsername();
        userId = user.getId();
        ps = positions.getPositions();

        if (ps != null) {
            // ordino e per validare le posizioni inserite
            ps.sort((o1, o2) ->
            {
                if (o1.getTimestamp() - o2.getTimestamp() >= 0)
                    return 1;
                else
                    return -1;
            });
            for (Position position : ps) {
                position.setUserId(userId);
                // validazione posizione
                condition = positionValidator.isValidPosition(position, username);
                totCount++;
                /*
                  se valida aggiunta dell'id all'archivio
                  e della posizione vera (flag true)
                  e della sua rappresentazione (flag false)
                 */
                if (condition) {
                    id = positionService.insertPosition(position);
                    if (id != null) {
                        count++;
                    }
                } else {
                    ps.remove(position);
                }
            }

            // se c'è almeno una pos valida creo archivio se no exception
            if (count > 0) {
                Archive archive = new Archive(userId, ps);
                String archiveId = insertArchive(archive);
                for (Position position : ps) {
                    // creazione archivio e set di id archivio in posizione
                    position.setArchiveId(archiveId);
                    position.setOnSale(true);
                    positionService.save(position);
                }

                return new StringResponse("Creato archivio con " + count + " posizioni valide su " + totCount);
            }
        }

        // no pos added
        return null;
    }

    public List<Position> getPositionsByArchiveId(String id) {
        return archiveRepository.findArchiveById(id).getPositions();
    }

    public boolean deleteArchiveById(String archiveId) {
        Archive archive = archiveRepository.findArchiveById(archiveId);
        // set not on sale for the archive
        archive.setOnSale(false);
        archiveRepository.save(archive);

        List<Position> positions = archive.getPositions();
        // set on sae false for all positions in archive
        for (Position position : positions) {
            position.setArchiveId(archiveId);
            position.setOnSale(false);
            positionService.save(position);
        }

        return true;
    }

    public ArchiveDownload getArchiveDownloadById(String archiveId) {
        Archive archive = archiveRepository.findArchiveById(archiveId);
        List<Position> archivePositions = archive.getPositions();
        List<PositionDownload> positionsDownload = new ArrayList<>();

        archivePositions.forEach(
                p -> positionsDownload.add(new PositionDownload(p.getId(), p.getLocation(), p.getTimestamp()))
        );

        return new ArchiveDownload(
                archiveId,
                archive.getUserId(),
                positionsDownload
        );
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
