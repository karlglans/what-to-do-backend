package com.karlglans.whattodo.repositories;

import com.karlglans.whattodo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  User save(User user);

  Optional<User> findUserBySub(String sub);
}
