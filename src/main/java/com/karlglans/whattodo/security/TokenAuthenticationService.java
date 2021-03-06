package com.karlglans.whattodo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class TokenAuthenticationService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Value("${token.secret}")
  String secret;

  @Value("${token.issuer}")
  String issuer;

  @Value("${token.users.expireTime}")
  Long usersExpireTime; // milliSeconds until token expire

  private Algorithm algorithm;
  private JWTVerifier verifier;

  @PostConstruct
  public void init() {
    this.algorithm = Algorithm.HMAC256(secret);
    this.verifier = JWT.require(algorithm)
      .withIssuer(issuer)
      .build(); //Reusable verifier instance
  }

  private Date makeExpireDate() {
    Date date = new Date();
    date.setTime(date.getTime() + usersExpireTime);
    return date;
  }

  private static int extractIdFromSubject(String subject) {
    // NOTE: for now sub is just user id. In the future it might be a hash or Guid
    return Integer.parseInt(subject);
  }

  SecurityUser extractUserFromToken(Object token) {
    String strToken = token.toString();
    DecodedJWT jwt;
    try {
      jwt = verifier.verify(strToken);
    } catch (JWTVerificationException exception) {
      logger.info(String.format("Verification failed for token %s", strToken));
      throw new BadCredentialsException("Missing Authentication Token");
    }

    SecurityUser authUser = new SecurityUser();
    authUser.setUsername("google-" + jwt.getSubject() );
    authUser.setSub(jwt.getSubject());

    return authUser;
  }

  public String createToken(SecurityUser userPrincipal) {
    return JWT.create()
      .withIssuer(issuer)
      .withClaim("exp", makeExpireDate())
      .withSubject(userPrincipal.getSub())
      .sign(algorithm);
  }

}
