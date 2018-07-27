package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.Archive;
import it.polito.ai.lab03.repository.model.Position;
import it.polito.ai.lab03.repository.model.Positions;
import it.polito.ai.lab03.repository.model.User;
import it.polito.ai.lab03.service.ArchiveService;
import it.polito.ai.lab03.service.PositionService;
import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import it.polito.ai.lab03.utils.IAuthorizationFacade;
import it.polito.ai.lab03.utils.PositionValidator;
import it.polito.ai.lab03.utils.StringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@RestController
@RequestMapping("/secured/manage")
public class ManagementController {

    private ArchiveService archiveService;
    private IAuthorizationFacade authorizationFacade;
    private UserDetailsServiceImpl userService;

    @Autowired
    public ManagementController(ArchiveService as, UserDetailsServiceImpl uds, IAuthorizationFacade iaf) {
        this.archiveService = as;
        this.authorizationFacade = iaf;
        this.userService = uds;
    }

    /**
     * Funzione per prendere tutte le posizioni dell'utente con il dato token
     *
     * @return List<Position> --> lista delle posizioni dell'utente
     */
    @RequestMapping(
            path = "/archives",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Archive> getAll() {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        return archiveService.getArchivesForUser(username);
    }

    /**
     * Ritorna la lista delle archive acquistate
     *
     * @return List<Archive> --> lista delle posizioni acquistate da un certo customer
     */
    @RequestMapping(
            path = "/archives/purchased",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Archive> getArchiveBought() {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        return archiveService.getArchivesBoughtCustomer(username);
    }

    /**
     * Funzione per aggiungere una posizione all'utente che possiede il token che ci viene dato con
     * la richiesta
     *
     * @param positions  --> è la posizione che vuole aggiungere l'utente
     */
    @RequestMapping(
            path = "/upload/archive",
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
                    "L'archivio inserito contiene solo posizioni invalide."
            );
        }

    }
}
