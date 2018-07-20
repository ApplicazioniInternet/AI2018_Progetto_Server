package it.polito.ai.lab03.utils;

import org.springframework.security.core.Authentication;

public interface IAuthorizationFacade {
    Authentication getAuthorization();
}
