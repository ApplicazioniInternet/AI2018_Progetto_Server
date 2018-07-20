package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.Position;
import it.polito.ai.lab03.repository.model.User;
import it.polito.ai.lab03.service.PositionService;
import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/secured/admin")
public class AdminController {

    private PositionService positionService;
    private UserDetailsServiceImpl userService;

    @Autowired
    public AdminController(PositionService ps, UserDetailsServiceImpl uds) {
        this.positionService = ps;
        this.userService = uds;
    }

    /**
     * Funzione per ritornare la collection di tutti gli users nel database
     *
     * @return List<User> --> lista degli user nel database
     */
    @RequestMapping(
            path = "/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<User> getAllUsers() {
        return userService.getAll();
    }

    /**
     * Funzione per ritornare uno specifico user
     *
     * @param user --> id dello user da ritornare
     * @return User --> lo user altrimenti null se non trova null
     */
    @RequestMapping(
            path = "/users/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    User getUser(@PathVariable(value = "id") String user) {
        return userService.getUser(user);
    }

    /**
     * Funzione per ritornare la collection di tutte le posizioni salvate nel nostro database
     * Non so però quanto sia utile, nel dubbio ce la lasciamo
     *
     * @return List<Position> --> lista delle posizioni
     */
    @RequestMapping(
            path = "/positions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Position> getAllPosition() {
        return positionService.getAll();
    }

    /**
     * Funzione per ritornare tutte le posizioni associate ad un utente
     *
     * @param user --> l'userId dell'utente specifico
     * @return List<Position> --> lista delle posizioni associate a tale utente
     */
    @RequestMapping(
            path = "users/{id}/positions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Position> getAllPositionForUser(@PathVariable(value = "id") String user) {
        return positionService.getPositionsForUser(user);
    }

}
