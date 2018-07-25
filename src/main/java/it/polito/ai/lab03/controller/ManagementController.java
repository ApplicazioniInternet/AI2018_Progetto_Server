package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.Position;
import it.polito.ai.lab03.repository.model.Positions;
import it.polito.ai.lab03.service.PositionService;
import it.polito.ai.lab03.utils.IAuthorizationFacade;
import it.polito.ai.lab03.utils.PositionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.List;

@RestController
@RequestMapping("/secured/user")
public class ManagementController {

    private PositionService positionService;
    private PositionValidator positionValidator;
    private IAuthorizationFacade authorizationFacade;

    @Autowired
    public ManagementController(PositionService ps, PositionValidator pv, IAuthorizationFacade iaf) {
        this.positionService = ps;
        this.positionValidator = pv;
        this.authorizationFacade = iaf;
    }

    /**
     * Funzione per prendere tutte le posizioni dell'utente con il dato token
     *
     * @return List<Position> --> lista delle posizioni dell'utente
     */
    @RequestMapping(
            path = "/positions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Position> getAll() {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        return positionService.getPositionsForUser(username);
    }

    /**
     * Funzione per aggiungere una posizione all'utente che possiede il token che ci viene dato con
     * la richiesta
     *
     * @param positions  --> è la posizione che vuole aggiungere l'utente
     */
    @RequestMapping(
            path = "/positions",
            method = RequestMethod.POST
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addPosition(@RequestBody Positions positions) {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        List<Position> ps;

        ps = positions.getPositions();
        ps.forEach( (position) -> {
            position.setUserId(username);
            boolean condition = positionValidator.isValidPosition(position, username);
            if (condition)
                positionService.insertPosition(position);
            else
                throw new NotAcceptableStatusException("La posizione inserita non è valida."); // 406
        });
    }
}
