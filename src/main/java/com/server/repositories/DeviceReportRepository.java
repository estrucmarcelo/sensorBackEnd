package com.server.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.models.DeviceReport;

@Repository
public interface DeviceReportRepository extends JpaRepository<DeviceReport, String> {
  @Query("SELECT r FROM DeviceReport r WHERE r.reportDate = :reportDate AND r.device.id = :deviceId ORDER BY r.reportTime")
  List<DeviceReport> getDeviceReports(@Param("deviceId") String deviceId, @Param("reportDate") LocalDate reportDate);
}
