package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.service.ArchiveService;
import it.polito.ai.lab03.service.PositionService;
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

    @Autowired
    public ArchiveController(ArchiveService as, PositionService ps,
                             UserDetailsServiceImpl uds, IAuthorizationFacade iaf) {
        this.archiveService = as;
        this.positionService = ps;
        this.authorizationFacade = iaf;
        this.userService = uds;
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
                    "L'archivio inserito non contiene posizioni valide "
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
     * Ritorna la lista degli archivi acquistati
     *
     * @param userId --> l'userId dell'utente specifico
     * @return List<Archive> --> lista degli archivi acquistati da user con userId
     */
    @RequestMapping(
            path = "/purchased/user/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Archive> getArchiveBought(@PathVariable(value = "id") String userId) {
        return archiveService.getArchivesBoughtCustomer(userId);
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
    long deleteArchive(@PathVariable(value = "id") String archiveId) {
        return archiveService.deleteArchiveById(archiveId);
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
        return archiveService.getArchiveDownloadById(archiveId);
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
        return positionService.getArchivesCount(locationRequest);
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
    List<ArchiveId> getArchivesList(@RequestBody AreaRequest locationRequest) {
        return positionService.getArchivesbyPositionsInArea(locationRequest);
    }
}
