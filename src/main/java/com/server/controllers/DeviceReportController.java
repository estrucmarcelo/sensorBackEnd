package com.server.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.dto.DeviceReportRequest;
import com.server.models.DeviceReport;
import com.server.models.DeviceReports;
import com.server.services.DeviceReportService;

@RestController
@RequestMapping("/api/devices/reports")
public class DeviceReportController {
  

	@Autowired
	private DeviceReportService reportService;

  @GetMapping
	public ResponseEntity<DeviceReports> getDeviceReports(@RequestParam("deviceId") String deviceId, @RequestParam("reportDate") String reportDate) {
		return reportService.getDeviceReports(deviceId, LocalDate.parse(reportDate, DateTimeFormatter.ISO_DATE));
	}

	@PostMapping
	public ResponseEntity<DeviceReport> createDeviceReport(@RequestBody DeviceReportRequest deviceReportRequest) {
		return reportService.createDeviceReport(deviceReportRequest);
	}
}
