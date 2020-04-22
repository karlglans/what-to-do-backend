package com.karlglans.whattodo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final TokenAuthenticationService tokenAuthenticationService;

  @Autowired
  public TokenAuthenticationProvider(TokenAuthenticationService tokenAuthenticationService) {
    this.tokenAuthenticationService = tokenAuthenticationService;
  }

  @Override
  protected void additionalAuthenticationChecks(final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
    // Nothing to do
  }

  @Override
  protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) {

    String token = String.valueOf(authentication.getCredentials());

    if (token.contains("null") || token.isEmpty()) {
      logger.info("malformed credentials header");
      throw new BadCredentialsException("Error!");
    }

    return tokenAuthenticationService.extractUserFromToken(token);
  }
}
