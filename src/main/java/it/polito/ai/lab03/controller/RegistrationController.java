package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import it.polito.ai.lab03.utils.StringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping(path = "/register")
public class RegistrationController {

    private UserDetailsServiceImpl userService;
    private PasswordEncoder userPasswordEncoder;

    @Autowired
    public RegistrationController(UserDetailsServiceImpl uds, PasswordEncoder pe) {
        this.userService = uds;
        this.userPasswordEncoder = pe;
    }

    /**
     * Funzione per ritornare la collection di tutti gli users nel database
     *
     * @return List<User> --> lista degli user nel database
     */
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    StringResponse register(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            @RequestParam("passwordConfirm") String passwordConfirm
                         ) {
        if (password.equals(passwordConfirm)) {
            if (userService.register(username, userPasswordEncoder.encode(password))) {
                return new StringResponse("Utente Creato");
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Nome utente già utilizzato"
                );
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Il campo 'Password' e 'Conferma Password' devono essere uguali"
            );
        }
    }
}
