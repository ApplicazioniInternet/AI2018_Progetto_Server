package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.AreaRequest;
import it.polito.ai.lab03.repository.model.Position;
import it.polito.ai.lab03.repository.model.Transaction;
import it.polito.ai.lab03.service.PositionService;
import it.polito.ai.lab03.service.TransactionService;
import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import it.polito.ai.lab03.utils.IAuthorizationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/secured/customer")
public class BuyController {

    private PositionService positionService;
    private TransactionService transactionService;
    private IAuthorizationFacade authorizationFacade;

    @Autowired
    public BuyController(PositionService ps, TransactionService ts, UserDetailsServiceImpl us, IAuthorizationFacade iaf) {
        this.positionService = ps;
        this.authorizationFacade = iaf;
        this.transactionService = ts;
    }

    /**
     * Ritorna la lista delle position acquistate
     *
     * @return List<Position> --> lista delle posizioni acquistate da un certo customer
     */
    @RequestMapping(
            path = "/positions/purchased",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Position> getPositionBought() {
        String username = authorizationFacade.getAuthorization().getPrincipal().toString();
        return positionService.getPositionsBoughtCustomer(username);
    }

    /**
     * Questo metodo poichè ritorna una lista di posizioni dato un poligono, deve essere l'acquisto
     * Due possibili ResponseStatus a seconda che lácquisto sia andato a buon fine o meno
     */
    @RequestMapping(
            path = "/positions/buy",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    List<Position> getPositionInArea(@RequestBody AreaRequest locationRequest) {
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

        return positionService.buyPositionsInArea(locationRequest, username);
    }

    @RequestMapping(
            path = "/positions/buy/count",
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
     * Funzione per ritornare la collection di tutte le posizioni salvate nel nostro database
     * Non so però quanto sia utile, nel dubbio ce la lasciamo
     *
     * @return List<Position> --> lista delle posizioni
     */
    @RequestMapping(
            path = "/buyable/positions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<Position> getAllPosition() {
        return positionService.getAll();
    }
}
