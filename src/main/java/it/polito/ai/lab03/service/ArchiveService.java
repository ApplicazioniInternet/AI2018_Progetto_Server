package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.ArchiveRepository;
import it.polito.ai.lab03.repository.TransactionRepository;
import it.polito.ai.lab03.repository.model.Archive;
import it.polito.ai.lab03.repository.model.AreaRequest;
import it.polito.ai.lab03.repository.model.Archive;
import it.polito.ai.lab03.repository.model.Transaction;
import it.polito.ai.lab03.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArchiveService {

    private ArchiveRepository archiveRepository;
    private TransactionRepository transactionRepository;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public ArchiveService(ArchiveRepository ar, UserDetailsServiceImpl uds, TransactionRepository tr) {
        this.archiveRepository = ar;
        this.transactionRepository = tr;
        this.userDetailsService = uds;

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
