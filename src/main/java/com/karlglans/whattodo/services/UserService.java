package com.karlglans.whattodo.services;

import com.karlglans.whattodo.entities.User;
import com.karlglans.whattodo.repositories.UserRepository;
import com.karlglans.whattodo.security.SecurityUser;
import com.karlglans.whattodo.services.exceptions.MissingUserException;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  UserRepository userRepo;

  public Integer getUserId() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    SecurityUser su = (SecurityUser) auth.getPrincipal();
    // shouldn't happen. An authorized user should also be stored
    return userRepo.findUserBySub(su.getSub())
            .orElseThrow(() -> new MissingUserException("missing user " + su.getSub()))
            .getId();
  }

  public User getUser() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    SecurityUser su = (SecurityUser) auth.getPrincipal();
    return userRepo.findUserBySub(su.getSub())
            .orElseThrow(() -> new MissingUserException("missing user " + su.getSub()));
  }
}
