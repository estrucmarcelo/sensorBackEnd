package com.server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.models.Device;
import com.server.models.DeviceType;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
  @Query("SELECT device FROM Device device WHERE structure.id = :structureId")
  List<Device> getDevicesByStructureId(@Param("structureId") String structureId);

  @Query("SELECT device FROM Device device WHERE device.name = :deviceName")
  Optional<Device> getDeviceByName(@Param("deviceName") String deviceName);

  @Query("SELECT device FROM Device device WHERE device.deviceType = :deviceType AND device.structure.id = :structureId ORDER BY device.createdAt")
  List<Device> getDevicesByType(@Param("structureId") String structureId, @Param("deviceType") DeviceType deviceType);
}
