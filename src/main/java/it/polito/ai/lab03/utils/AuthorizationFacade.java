package it.polito.ai.lab03.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFacade implements IAuthorizationFacade {
    @Override
    public Authentication getAuthorization() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
