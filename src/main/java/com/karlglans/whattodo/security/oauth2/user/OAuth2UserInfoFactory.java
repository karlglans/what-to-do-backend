package com.karlglans.whattodo.security.oauth2.user;

import lombok.var;

import java.util.Map;

public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
    var aaa = new GoogleOAuth2UserInfo(attributes);
    return new GoogleOAuth2UserInfo(attributes);
  }
}

