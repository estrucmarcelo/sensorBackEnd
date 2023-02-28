package com.server.advice;

import java.util.Date;

import lombok.Getter;

public class ErrorMessage {
  @Getter
  private int statusCode;
  
  @Getter
  private Date timestamp;
  
  @Getter
  private String message;
  
  @Getter
  private String description;

  public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
    this.statusCode = statusCode;
    this.timestamp = timestamp;
    this.message = message;
    this.description = description;
  }
}