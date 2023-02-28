package com.server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  @Query("SELECT user FROM User user WHERE user.email = :email")
  Optional<User> getUserByEmail(@Param("email") String email);
}
