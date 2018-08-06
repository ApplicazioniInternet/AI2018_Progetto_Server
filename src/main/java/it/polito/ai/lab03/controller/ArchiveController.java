package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.repository.model.archive.Archive;
import it.polito.ai.lab03.repository.model.archive.ArchiveDownload;
import it.polito.ai.lab03.repository.model.archive.ArchiveLight;
import it.polito.ai.lab03.repository.model.archive.ArchiveLightList;
import it.polito.ai.lab03.repository.model.position.Position;
import it.polito.ai.lab03.repository.model.position.Positions;
import it.polito.ai.lab03.repository.model.transaction.Transaction;
import it.polito.ai.lab03.service.ArchiveService;
import it.polito.ai.lab03.service.PositionService;
import it.polito.ai.lab03.service.TransactionService;
import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import it.polito.ai.lab03.utils.IAuthorizationFacade;
import it.polito.ai.lab03.utils.StringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/secured/archives")
public class ArchiveController {

    private ArchiveService archiveService;
    private PositionService positionService;
    private IAuthorizationFacade authorizationFacade;
    private UserDetailsServiceImpl userService;
    private TransactionService transactionService;

    @Autowired
    public ArchiveController(ArchiveService as, PositionService ps,
                             UserDetailsServiceImpl uds, IAuthorizationFacade iaf,
                             TransactionService ts) {
        this.archiveService = as;
        this.positionService = ps;
        this.authorizationFacade = iaf;
        this.userService = uds;
        this.transactionService = ts;
    }

    /**
     * Funzione per ritornare la collection di tutte le posizioni salvate nel nostro database
     * Non so però quanto sia utile, nel dubbio ce la lasciamo
     *
     * @return List<Archive> --> lista delle posizioni
     */
    @RequestMapping(
            path = "/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Archive> getAllArchive() {
        return archiveService.getAll();
    }

    /**
     * Funzione per ritornare tutte gli archivi associati ad un utente
     *
     * @param userId --> l'userId dell'utente specifico
     * @return List<Archive> --> lista degli archivi associati a tale utente
     */
    @RequestMapping(
            path = "uploaded/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Archive> getAllArchiveForUser(@PathVariable(value = "id") String userId) {
        return archiveService.getArchivesForUser(userId);
    }

    /**
     * Funzione per ritornare tutte gli archivi associati ad un utente
     *
     * @param userId --> l'userId dell'utente specifico
     * @return List<Archive> --> lista degli archivi associati a tale utente
     */
    @RequestMapping(
            path = "purchased/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Archive> getAllArchiveBoughtForUser(@PathVariable(value = "id") String userId) {
        return archiveService.getArchivesBoughtForUser(userId);
    }

    /**
     * Funzione per aggiungere una posizione all'utente che possiede il token che ci viene dato con
     * la richiesta
     *
     * @param positions --> è la lista di posizioni che  l'utente vuole aggiungere all'archivio
     */
    @RequestMapping(
            path = "/archive/upload",
            method = RequestMethod.POST
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public StringResponse addArchive(@RequestBody Positions positions) {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        User user = userService.getUser(username);
        StringResponse resp = archiveService.uploadArchive(user, positions);
        if (resp != null) {
            return resp;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The archive does not contain valid positions"
            );
        }

    }

    /**
     * Funzione per prendere tutte le pos da archivi
     *
     * @param id --> archiveId
     */
    @RequestMapping(
            path = "/archive/{id}/positions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Position> getPositionsInArchive(@PathVariable(value = "id") String id) {
        return archiveService.getPositionsByArchiveId(id);
    }

    /**
     * Funzione per prendere tutte le pos da archivi
     *
     * @param id --> archiveId
     */
    @RequestMapping(
            path = "/archive/{id}/sales",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    int getArchiveSales(@PathVariable(value = "id") String id) {
        return archiveService.getArchiveSalesCount(id);
    }

    /**
     * Questo metodo poichè ritorna una lista di rappresentazioni dato un poligono
     */
    @RequestMapping(
            path = "/buy",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Transaction buyArchives(@RequestBody ArchiveLightList archiveLightList) {
        if (archiveLightList.getArchiveList().size() > 0) {
            String username = authorizationFacade.getAuthorization().getPrincipal().toString();
            String userId = userService.getUser(username).getId();
            return transactionService.buyArchives(archiveLightList, userId);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Archive list can not be empty"
            );
        }
    }

    /**
     * Ritorna l'archivio eliminato
     *
     * @param archiveId --> id dell'archivio da eliminare
     * @return Archive --> archivio eliminato
     */
    @RequestMapping(
            path = "archive/{id}/delete",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    StringResponse deleteArchive(@PathVariable(value = "id") String archiveId) {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        String userId = userService.getUser(username).getId();
        if (archiveService.deleteArchiveById(archiveId, userId))
            return new StringResponse("Archive deleted");
        else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Archive deletion is forbidden for users that are not the owner"
            );
        }
    }

    /**
     * Ritorna l'archivio
     *
     * @param archiveId --> id dell'archivio
     * @return Archive --> archivio scelto
     */
    @RequestMapping(
            path = "archive/{id}/download",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    ArchiveDownload downloadArchive(@PathVariable(value = "id") String archiveId) {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        String userId = userService.getUser(username).getId();
        ArchiveDownload ad = archiveService.getArchiveDownloadById(archiveId, userId);
        if (ad != null)
            return ad;
        else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Archive download is forbidden for users that are not the owner or buyers"
            );
        }
    }

    /**
     * Questo metodo poichè ritorna una lista di rappresentazioni dato un poligono
     */
    @RequestMapping(
            path = "/area/count",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    int getArchivesCount(@RequestBody AreaRequest locationRequest) {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        String userId = userService.getUser(username).getId();
        return positionService.getArchivesCount(locationRequest, userId);
    }

    /**
     * Questo metodo poichè ritorna una lista di rappresentazioni dato un poligono
     */
    @RequestMapping(
            path = "/area/list",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<ArchiveLight> getArchivesList(@RequestBody AreaRequest locationRequest) {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        String userId = userService.getUser(username).getId();
        return positionService.getArchivesbyPositionsInArea(locationRequest, userId);
    }
}
