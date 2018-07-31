package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.User;
import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/secured/users")
public class UserController {

    private UserDetailsServiceImpl userService;

    @Autowired
    public UserController(UserDetailsServiceImpl uds) {
        this.userService = uds;
    }

    /**
     * Funzione per ritornare la collection di tutti gli users nel database
     *
     * @return List<User> --> lista degli user nel database
     */
    @RequestMapping(
            path = "/",
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
            path = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    User getUser(@PathVariable(value = "id") String user) {
        return userService.getUser(user);
    }

}
