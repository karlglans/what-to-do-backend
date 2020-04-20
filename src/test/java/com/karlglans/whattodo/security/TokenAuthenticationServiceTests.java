package com.karlglans.whattodo.security;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TokenAuthenticationServiceTests {
  @Autowired
  TokenAuthenticationService tokenAuthenticationService;

  @Test
  public void canMakeToken() {
    SecurityUser securityUser = new SecurityUser();
    securityUser.setSub("123");
    String token = tokenAuthenticationService.createToken(securityUser);
    Assert.assertNotNull(token);

  }
}
