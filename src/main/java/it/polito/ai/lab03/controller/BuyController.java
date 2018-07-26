package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.service.ArchiveService;
import it.polito.ai.lab03.service.PositionService;
import it.polito.ai.lab03.service.TransactionService;
import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import it.polito.ai.lab03.utils.IAuthorizationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/secured/buy")
public class BuyController {

    private PositionService positionService;
    private ArchiveService archiveService;
    private TransactionService transactionService;
    private IAuthorizationFacade authorizationFacade;
    private UserDetailsServiceImpl userService;

    @Autowired
    public BuyController(PositionService ps, ArchiveService as, UserDetailsServiceImpl uds,
                         TransactionService ts, IAuthorizationFacade iaf) {
        this.archiveService = as;
        this.positionService = ps;
        this.authorizationFacade = iaf;
        this.transactionService = ts;
        this.userService = uds;
    }

    /**
     * Questo metodo poichè ritorna una lista di posizioni dato un poligono, deve essere l'acquisto
     * Due possibili ResponseStatus a seconda che lácquisto sia andato a buon fine o meno
     */
    @RequestMapping(
            path = "/archives/buy",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    List<Archive> getArchivesInArea(@RequestBody AreaRequest locationRequest) {
        //Ricavo l'username per passarlo al service layer in modo da settare il buyer nella transazione
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        /*
        Bisognerebbe verificare se la transazione è andata a buon fine e in caso contrario dare un messaggio di errore.
        Possibili errori:
        - area non valida (bad request)
        - nessuna posizione in quell'area (si può anche tornare un ok e lista vuota)
        - soldi non sufficienti per transazione (che errore? forse forbidden?)
        - transazione fallita (internal server error)
         */
        List<String> archivesId = null/* todo populate archives*/;
        return archiveService.buyArchives(archivesId, username);
    }

    @RequestMapping(
            path = "/positions/count",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    int getPositionQuantityInArea(@RequestBody AreaRequest locationRequest) {
        return positionService.getNumberPositionsInArea(locationRequest);
    }


    @RequestMapping(
            path = "/transactions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Transaction> getTransactions() {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        return transactionService.getTransactionsPerUser(username);
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
     * Funzione per ritornare tutte le posizioni associate ad un utente
     *
     * @param user --> l'userId dell'utente specifico
     * @return List<Archive> --> lista delle posizioni associate a tale utente
     */
    @RequestMapping(
            path = "users/{id}/archives",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Archive> getAllArchiveForUser(@PathVariable(value = "id") String user) {
        return archiveService.getArchivesForUser(user);
    }


    /**
     * Funzione per ritornare la collection di tutte le posizioni salvate nel nostro database
     * Non so però quanto sia utile, nel dubbio ce la lasciamo
     *
     * @return List<Archive> --> lista delle posizioni
     */
    @RequestMapping(
            path = "/archives",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Archive> getAllArchive() {
        return archiveService.getAll();
    }

}
