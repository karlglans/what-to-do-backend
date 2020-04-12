package com.karlglans.whattodo.security;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TokenAuthenticationServiceTests {
  @Autowired
  TokenAuthenticationService aaa;


  @Test
  public void equalOperator() {

    SecurityUser securityUser = new SecurityUser();
    securityUser.setSub("123");
    String token = aaa.createToken(securityUser);
    Assert.assertNotNull(token);

  }
}
