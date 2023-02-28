package com.server.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

  @Id()
  @GeneratedValue(strategy = GenerationType.UUID)
  @Getter
  private String id;

  @Column(unique = true)
  @Getter()
  private String email;

  @Column()
  @Getter
  private String password;

  // @ManyToMany(fetch = FetchType.LAZY)
  // @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  // @Getter
  // @Setter
  // private Set<Role> roles = new HashSet<Role>();;

  public User() {}

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
