package com.server.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.server.dto.DeviceCreateRequest;
import com.server.dto.MessageResponse;
import com.server.models.Device;
import com.server.models.DeviceType;
import com.server.models.Structure;
import com.server.repositories.DeviceRepository;
import com.server.repositories.StructureRepository;

@Service
public class DeviceService {
  @Autowired
  private DeviceRepository deviceRepository;

  @Autowired
  private StructureRepository structureRepository;

  public ResponseEntity<List<Device>> getDevices(String structureId) {
    List<Device> devices = deviceRepository.getDevicesByStructureId(structureId);

    return new ResponseEntity<List<Device>>(devices, HttpStatus.OK);
  }

  public ResponseEntity<?> getDevicesByType(String structureId, String deviceType) {
    Optional<Structure> structureFound = structureRepository.findById(structureId);

    if(!structureFound.isPresent()) {
      return ResponseEntity.badRequest().body(new MessageResponse("Structure not found."));
    }

    DeviceType type = DeviceType.valueOf(deviceType.toUpperCase());

    if(type == null) {
      return ResponseEntity.badRequest().body(new MessageResponse("Device type not found."));
    }

    List<Device> devices = deviceRepository.getDevicesByType(structureId, type);

    return new ResponseEntity<List<Device>>(devices, HttpStatus.OK);
  }

  public ResponseEntity<Device> getDeviceById(String id) {
    Optional<Device> deviceFound = deviceRepository.findById(id);

    if (!deviceFound.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<Device>(deviceFound.get(), HttpStatus.OK);
  }

  public ResponseEntity<?> createDevice(DeviceCreateRequest createRequest) {
    try {
      Optional<Device> deviceExists = deviceRepository.getDeviceByName(createRequest.getName());

      if(deviceExists.isPresent()) {
        return ResponseEntity.badRequest().body(new MessageResponse("A device with this name already exists."));
      }

      Structure structure = structureRepository.getReferenceById(createRequest.getStructureId());

      if(structure == null) {
        return ResponseEntity.badRequest().body(new MessageResponse("Structure not found."));
      }

      DeviceType deviceType = DeviceType.valueOf(createRequest.getDeviceType().toUpperCase());

      if(deviceType == null) {
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid device type."));
      }

      Device device = deviceRepository.save(new Device(structure, deviceType, createRequest.getName(), createRequest.getDescription()));

			return new ResponseEntity<>(device, HttpStatus.CREATED);
		} catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse("Internal server error."));
		}
  }
}
