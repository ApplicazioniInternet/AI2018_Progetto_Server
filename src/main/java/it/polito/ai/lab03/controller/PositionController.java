package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.*;
import it.polito.ai.lab03.repository.model.position.PositionRepresentationDownload;
import it.polito.ai.lab03.service.ArchiveService;
import it.polito.ai.lab03.service.PositionService;
import it.polito.ai.lab03.utils.IAuthorizationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/secured/positions")
public class PositionController {

    private ArchiveService archiveService;
    private IAuthorizationFacade authorizationFacade;
    private PositionService positionService;

    @Autowired
    public PositionController(ArchiveService as, PositionService ps, IAuthorizationFacade iaf) {
        this.archiveService = as;
        this.authorizationFacade = iaf;
        this.positionService = ps;
    }

    /**
     * Questo metodo poichè ritorna una lista di rappresentazioni dato un poligono
     */
    @RequestMapping(
            path = "/representations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PositionRepresentationDownload getPositionsRepresentations(@RequestBody AreaRequest locationRequest) {
        return positionService.getRepresentations(locationRequest);
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
    int getArchiveCount(@RequestBody AreaRequest locationRequest) {
        return positionService.getPositionsCount(locationRequest);
    }
}
