package com.server.dto;

import lombok.Getter;

public class DeviceCreateRequest {
  @Getter
  private String structureId;

  @Getter
  private String deviceType;

  @Getter
  private String name;

  @Getter
  private String description;

  public DeviceCreateRequest(String structureId, String deviceType, String name, String description) {
    this.structureId = structureId;
    this.deviceType = deviceType;
    this.name = name;
    this.description = description;
  }
}
