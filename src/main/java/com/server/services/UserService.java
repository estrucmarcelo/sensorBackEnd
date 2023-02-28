package com.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.server.models.User;
import com.server.repositories.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User getUser(String id) {
    Optional<User> userFound = userRepository.findById(id);

    if (!userFound.isPresent()) {
      return null;
    }

    return userFound.get();
  }

  public ResponseEntity<User> getUserById(String id) {
    Optional<User> userFound = userRepository.findById(id);

    if (!userFound.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<User>(userFound.get(), HttpStatus.OK);
  }
}