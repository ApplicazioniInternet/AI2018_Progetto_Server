package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.Archive;
import it.polito.ai.lab03.repository.model.Position;
import it.polito.ai.lab03.repository.model.Positions;
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

@RestController
@RequestMapping("/secured/manage")
public class ManagementController {

    private PositionService positionService;
    private ArchiveService archiveService;
    private PositionValidator positionValidator;
    private IAuthorizationFacade authorizationFacade;
    private UserDetailsServiceImpl userService;

    @Autowired
    public ManagementController(PositionService ps, ArchiveService as, UserDetailsServiceImpl uds,
                                PositionValidator pv, IAuthorizationFacade iaf) {
        this.archiveService = as;
        this.positionService = ps;
        this.positionValidator = pv;
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
        String userId = userService.getUser(username).getId();
        List<Position> ps;
        String id;
        boolean condition;
        int count = 0;
        int totCount = 0;
        long timestamp = 0;
        double lng = 0;
        double lat = 0;
        List<String> positionsId = new ArrayList<>();
        List<Position> positionsToAdd = new ArrayList<>();
        List<Position> representationsByTime = new ArrayList<>();
        List<Position> representationsByPositions = new ArrayList<>();
        Position tmp;

        ps = positions.getPositions();
        for(int i = 0; i < ps.size(); i++) {
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
                position.setRealPosition(true);
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
            String archiveId = archiveService.insertArchive(archive);
            for(int i = 0; i < positionsToAdd.size(); i++) {
                Position position = positionsToAdd.get(i);
                position.setArchiveId(archiveId);
            }

            /**
             * todo decidere se spostare tutto da qua (probabilmente sarebbe saggio)
             * todo creare posizioni rappresentative per time e pos con id arch
             * positionsToAdd.sort(Comparator.comparingLong(Position::getTimestamp));
             *
            */

            return new StringResponse("Creato archivio con " + count + " posizioni valide su " + totCount);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'archivio inserito contiene solo posizioni invalide."
            );
        }

    }
}
