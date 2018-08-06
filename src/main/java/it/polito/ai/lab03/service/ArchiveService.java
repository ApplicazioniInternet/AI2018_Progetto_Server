package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.ArchiveRepository;
import it.polito.ai.lab03.repository.TransactionRepository;
import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.repository.model.archive.Archive;
import it.polito.ai.lab03.repository.model.archive.ArchiveDownload;
import it.polito.ai.lab03.repository.model.archive.ArchiveLight;
import it.polito.ai.lab03.repository.model.position.Position;
import it.polito.ai.lab03.repository.model.position.PositionDownload;
import it.polito.ai.lab03.repository.model.position.Positions;
import it.polito.ai.lab03.repository.model.transaction.Transaction;
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

    public List<Archive> getArchivesBoughtForUser(String userId) {
        List<Transaction> transactions =  this.transactionRepository.findAllByBuyerId(userId);
        List<Archive> archives = new ArrayList<>();
        transactions.forEach(
                transaction ->
                    transaction.getArchivesBought().getArchiveList().forEach(
                            archiveLight ->
                                archives.add(archiveRepository.findArchiveById(archiveLight.getArchiveId()))
                            )
                );
        return archives;
    }

    private String insertArchive(Archive archive) {
        archiveRepository.insert(archive);
        return archive.getId();
    }

    @Transactional
    public StringResponse uploadArchive(User user, Positions positions) {
        List<Position> ps;
        List<Position> posToAdd = new ArrayList<>();

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
                condition = positionValidator.isValidPosition(position, userId);
                totCount++;
                /*
                  se valida aggiunta dell'id all'archivio
                  e della posizione vera (flag true)
                  e della sua rappresentazione (flag false)
                 */
                if (condition) {
                    id = positionService.insertPosition(position);
                    if (id != null) {
                        posToAdd.add(position);
                        count++;
                    }
                }
            }

            // se c'è almeno una pos valida creo archivio se no exception
            if (count > 0) {
                Archive archive = new Archive(userId, posToAdd);
                String archiveId = insertArchive(archive);
                for (Position position : posToAdd) {
                    // creazione archivio e set di id archivio in posizione
                    position.setArchiveId(archiveId);
                    position.setOnSale(true);
                    positionService.save(position);
                }

                return new StringResponse("Archive created with " + count + " valid positions out of " + totCount);
            }
        }

        // no pos added
        return null;
    }

    public List<Position> getPositionsByArchiveId(String id) {
        return archiveRepository.findArchiveById(id).getPositions();
    }

    public boolean deleteArchiveById(String archiveId, String userId) {
        Archive archive = archiveRepository.findArchiveById(archiveId);
        if (archive.getUserId().equals(userId)) {
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
        } else {
            return false;
        }
    }

    public ArchiveDownload getArchiveDownloadById(String archiveId, String userId) {
        boolean allowed = false;
        Archive archive = archiveRepository.findArchiveById(archiveId);

        if (archive.getUserId().equals(userId))
            allowed = true;
        else {
            List<Transaction> trs = transactionRepository.
                    findByBuyerIdAndArchivesBoughtArchiveListContains(userId, new ArchiveLight(archiveId));
            if (!(trs).isEmpty())
                allowed = true;
        }

        if (allowed) {
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
        } else
            return null;
    }

    public int getArchiveSalesCount(String id) {
        Archive archive = archiveRepository.findArchiveById(id);
        List<Transaction> transactions = archive.getTransactions();
        if (transactions != null)
            return transactions.size();
        else
            return 0;
    }
}
