package com.server.models;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "devices")
public class Device {
  @Id()
  @GeneratedValue(strategy = GenerationType.UUID)
  @Getter
  private String id;

  @Enumerated(EnumType.STRING)
  @Column
  @Getter
  private DeviceType deviceType;

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

  @ManyToOne
  @JoinColumn(name = "structure_id", referencedColumnName = "id")
  private Structure structure;

  @OneToMany(mappedBy = "device")
  private List<DeviceReport> deviceReports;

  public Device() {}

  public Device(Structure structure, DeviceType deviceType, String name, String description) {
    this.structure = structure;
    this.deviceType = deviceType;
    this.name = name;
    this.description = description;
  }
}
