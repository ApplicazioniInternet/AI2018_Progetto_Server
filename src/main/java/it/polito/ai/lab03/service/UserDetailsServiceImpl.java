package it.polito.ai.lab03.service;

import it.polito.ai.lab03.repository.UserRepository;
import it.polito.ai.lab03.repository.model.position.Position;
import it.polito.ai.lab03.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Questo service ci permette di tirare su dal database tutte le informazioni per uno specifico
 * utente.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository ur) {
        this.userRepository = ur;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException(username);
    }

    public String getIdByUsername (String username) {
        return userRepository.findByUsername(username).getId();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUser(String user) {
        return userRepository.findByUsername(user);
    }

    void updateByUsernamePositions(String buyer, List<Position> positions) {
        User user = userRepository.findByUsername(buyer);
        user.setPositions(positions);

        userRepository.save(user);
    }

    public boolean register(String username, String password) {
        if ( userRepository.findByUsername(username) != null )
            return false;
        else {
            User user = new User(username, password);
            userRepository.save(user);
            return true;
        }
    }
}
