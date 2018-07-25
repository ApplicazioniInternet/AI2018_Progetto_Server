package it.polito.ai.lab03.controller;

import it.polito.ai.lab03.repository.model.User;
import it.polito.ai.lab03.service.PositionService;
import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            method = RequestMethod.POST
    )
    public @ResponseBody
    ResponseEntity<String> register( @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("passwordConfirm") String passwordConfirm
                         ) {
        if (password.equals(passwordConfirm)) {
            if (userService.register(username, userPasswordEncoder.encode(password))) {
                return new ResponseEntity<>("Utente creato", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Nome utente già utilizzato", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Il campo 'password' e 'conferma password' devono essere uguali", HttpStatus.BAD_REQUEST);
        }
    }
}
