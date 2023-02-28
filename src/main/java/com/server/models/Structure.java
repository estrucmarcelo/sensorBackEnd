package com.server.models;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "structures")
public class Structure {
  @Id()
  @GeneratedValue(strategy = GenerationType.UUID)
  @Getter
  private String id;

  @Column()
  @Getter
  private String name;

  @Column()
  @Getter
  private String description;

  @Column(name = "created_at")
  @CreationTimestamp
  @Getter
  private Date createdAt;

  @OneToMany(mappedBy = "structure")
  private List<Device> devices;

  public Structure() {}

  public Structure(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
