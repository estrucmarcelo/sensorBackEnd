package com.server.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.server.dto.MessageResponse;
import com.server.models.Structure;
import com.server.repositories.StructureRepository;

@Service
public class StructureService {
  @Autowired
  private StructureRepository structureRepository;

  public ResponseEntity<List<Structure>> getStructures() {
    List<Structure> structures = structureRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));

    return new ResponseEntity<List<Structure>>(structures, HttpStatus.OK);
  }

  public ResponseEntity<?> getStructureById(String id) {
    Optional<Structure> structureFound = structureRepository.findById(id);

    if (!structureFound.isPresent()) {
      return ResponseEntity.badRequest().body(new MessageResponse("Structure not found."));
    }

    return new ResponseEntity<Structure>(structureFound.get(), HttpStatus.OK);
  }

  public ResponseEntity<?> createStructure(Structure createStructure) {
    try {
      Optional<Structure> structureExists = structureRepository.getStructureByName(createStructure.getName());

      if(structureExists.isPresent()) {
        return ResponseEntity.badRequest().body(new MessageResponse("A structure with this name already exists."));
      }

      Structure structure = structureRepository
          .save(new Structure(createStructure.getName(), createStructure.getDescription()));

      return new ResponseEntity<>(structure, HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse("Internal server error."));
    }
  }
}
