package com.karlglans.whattodo.security;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  TokenAuthenticationService tokenAuthenticationService;

  @Override
  protected void additionalAuthenticationChecks(final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
    // Nothing to do
  }

  @Override
  protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) {

    String token = String.valueOf(authentication.getCredentials());

    if (token.contains("null") || token.isEmpty()) {
      throw new BadCredentialsException("Error!");
    }

    return tokenAuthenticationService.extractUserFromToken(token);
  }
}
