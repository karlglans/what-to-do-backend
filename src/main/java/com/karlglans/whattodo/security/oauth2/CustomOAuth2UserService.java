package com.karlglans.whattodo.security.oauth2;

import com.karlglans.whattodo.entities.User;
import com.karlglans.whattodo.repositories.UserRepository;
import com.karlglans.whattodo.security.SecurityUser;
import com.karlglans.whattodo.security.oauth2.user.OAuth2UserInfo;
import com.karlglans.whattodo.security.oauth2.user.OAuth2UserInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final UserRepository userRepository;

  @Autowired
  public CustomOAuth2UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

    try {
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      logger.error("failed to process user from oath");
      // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  /**
   * Will store the user if its not already existing in system
   * @param sub subscription id, from OpenId provider
   * @return a security principal
   */
  private SecurityUser storeUser(String sub) {
    Optional<User> optUser = userRepository.findUserBySub(sub);
    SecurityUser userPrincipal = new SecurityUser();
    userPrincipal.setEmail("processOAuth2UserEmail");
    userPrincipal.setUsername("processOAuth2UserName");
    userPrincipal.setSub(sub);
    if (!optUser.isPresent()) {
      User user = new User();
      user.setSub(sub);
      userRepository.save(user);
    }
    return userPrincipal;
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
    // Maybe remove since we are not storing emails
    if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
    }
    return storeUser((String) oAuth2UserInfo.getAttributes().get("sub"));
  }

}
