package com.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.dto.DeviceCreateRequest;
import com.server.models.Device;
import com.server.models.DeviceType;
import com.server.services.DeviceService;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
  @Autowired
  private DeviceService deviceService;

	@GetMapping("/all")
	public ResponseEntity<List<Device>> getDevices(@RequestParam("structureId") String structureId) {
		return deviceService.getDevices(structureId);
	}


  @GetMapping
	public ResponseEntity<Device> getDeviceById(@RequestParam("id") String id) {
		return deviceService.getDeviceById(id);
	}

  @GetMapping("/type/{deviceType}")
	public ResponseEntity<?> getDevicesByType(@Param("structureId") String structureId, @PathVariable("deviceType") String deviceType) {
		return deviceService.getDevicesByType(structureId, deviceType);
	}

	@PostMapping
	public ResponseEntity<?> createDevice(@RequestBody DeviceCreateRequest createRequest) {
		return deviceService.createDevice(createRequest);
	}
}
