package com.server.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.Getter;

public class DeviceReports {
  @Getter
  private int average;

  @Getter
  private int lowest;

  @Getter
  private int highest;

  @Getter
  private LocalDate reportDate;

  @Getter
  private List<DeviceReport> reports;

  public DeviceReports(int average, int lowest, int highest, LocalDate reportDate, List<DeviceReport> reports) {
    this.average = average;
    this.lowest = lowest;
    this.highest = highest;
    this.reportDate = reportDate;
    this.reports = reports;
  }
}
