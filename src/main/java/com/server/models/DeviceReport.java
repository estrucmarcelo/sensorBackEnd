package com.server.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "device_reports")
public class DeviceReport {
  @Id()
  @GeneratedValue(strategy = GenerationType.UUID)
  @Getter
  private String id;

  @ManyToOne
  @JoinColumn(name = "device_id", referencedColumnName = "id")
  private Device device;

  @Column()
  @Getter
  private int scale;

  @JsonIgnoreProperties({"reportDate"})
  @Column(name = "report_date")
  @Getter
  private LocalDate reportDate;

  @Column(name = "report_time")
  @Getter
  private LocalDateTime reportTime;

  public DeviceReport() {}

  public DeviceReport(Device device, int scale, LocalDate reportDate, LocalDateTime reportTime) {
    this.device = device;
    this.scale = scale;
    this.reportTime = reportTime;
    this.reportDate = reportDate;
  }
}
