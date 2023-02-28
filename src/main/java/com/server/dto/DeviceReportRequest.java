package com.server.dto;

import java.time.LocalDateTime;

import lombok.Getter;

public class DeviceReportRequest {

  @Getter
  private String deviceId;

  @Getter
  private int scale;

  @Getter
  private LocalDateTime reportTime;

  public DeviceReportRequest(String deviceId, int scale, LocalDateTime reportTime) {
    this.deviceId = deviceId;
    this.scale = scale;
    this.reportTime = reportTime;
  }
}
