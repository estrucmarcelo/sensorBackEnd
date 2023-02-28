package com.server.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.server.dto.DeviceReportRequest;
import com.server.models.Device;
import com.server.models.DeviceReport;
import com.server.models.DeviceReports;
import com.server.repositories.DeviceReportRepository;
import com.server.repositories.DeviceRepository;

@Service
public class DeviceReportService {
  @Autowired
  private DeviceReportRepository deviceReportRepository;

  @Autowired
  private DeviceRepository deviceRepository;

  public ResponseEntity<DeviceReport> createDeviceReport(DeviceReportRequest deviceReportRequest) {
    try {
      Device deviceFound = deviceRepository.getReferenceById(deviceReportRequest.getDeviceId());

      if(deviceFound == null) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
      }

      DeviceReport deviceReport = deviceReportRepository.save(new DeviceReport(deviceFound, deviceReportRequest.getScale(), deviceReportRequest.getReportTime().toLocalDate(), deviceReportRequest.getReportTime()));

			return new ResponseEntity<>(deviceReport, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
  }

  public ResponseEntity<DeviceReports> getDeviceReports(String deviceId, LocalDate reportDate) {
    List<DeviceReport> reports = deviceReportRepository.getDeviceReports(deviceId, reportDate);

    List<DeviceReport> sortedReports = reports.stream().sorted((r1, r2) -> r2.getScale() - r1.getScale()).collect(Collectors.toList());

    DeviceReport lowestReport = !reports.isEmpty() ? sortedReports.get(sortedReports.size() - 1) : null;

    DeviceReport highestReport = !reports.isEmpty() ? sortedReports.get(0) : null;

    int scaleSum = reports.stream().mapToInt(deviceReport -> deviceReport.getScale()).sum();

    int averageScale = reports.isEmpty() ? 0 : (int) Math.floor(((double) scaleSum) / reports.size());

    return new ResponseEntity<DeviceReports>(new DeviceReports(averageScale, lowestReport != null ? lowestReport.getScale() : 0, highestReport != null ? highestReport.getScale() : 0, reportDate, reports), HttpStatus.ACCEPTED);
  }
}
