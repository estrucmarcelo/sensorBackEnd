package com.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.models.Structure;
import com.server.services.StructureService;

@RestController
@RequestMapping("/api/structures")
public class StructureController {
  @Autowired
  private StructureService structureService;

	@GetMapping("/all")
	public ResponseEntity<List<Structure>> getStructures() {
		return structureService.getStructures();
	}

  @GetMapping
	public ResponseEntity<?> getStructureById(@RequestParam("id") String id) {
		return structureService.getStructureById(id);
	}

	@PostMapping
	public ResponseEntity<?> createStructure(@RequestBody Structure createRequest) {
		return structureService.createStructure(createRequest);
	}
}
