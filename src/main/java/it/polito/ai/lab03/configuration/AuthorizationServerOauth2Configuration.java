package it.polito.ai.lab03.configuration;

import it.polito.ai.lab03.service.UserDetailsServiceImpl;
import it.polito.ai.lab03.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * Conterrà:
 * 1) una classe per prendere i dati delle credenziali dal pool degli utenti;
 * 2) un authentication manager in grado di controllare le credenziali e settare il ruolo opportuno.
 *
 * Queste classi poi saranno restituite tramite metodi annotati con @Bean
 */
@Configuration
@EnableAuthorizationServer
@Import(ServerSecurityConfig.class)
public class AuthorizationServerOauth2Configuration extends AuthorizationServerConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder oauthClientPasswordEncoder;

    @Autowired
    public AuthorizationServerOauth2Configuration(UserDetailsServiceImpl udsi, AuthenticationManager am, PasswordEncoder pe) {
        this.userDetailsService = udsi;
        this.authenticationManager = am;
        this.oauthClientPasswordEncoder = pe;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public OAuth2AccessDeniedHandler oauthAccessDeniedHandler() {
        // Così ci pensa lui per i fatti suoi
        return new OAuth2AccessDeniedHandler();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").passwordEncoder(oauthClientPasswordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), accessTokenConverter()));

        endpoints.tokenStore(tokenStore()) // Usa il token dal database
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager) // Usa il mio custom authentication manager per autenticare gli utenti
                .userDetailsService(userDetailsService); // Come data source prendi quella da userDetails service
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(Constants.CLIENT_ID)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                .scopes("read", "write")
                .secret(Constants.CLIENT_SECRET);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(Constants.SIMMETRIC_KEY);
        converter.setVerifierKey(Constants.SIMMETRIC_KEY);
        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}
