package com.server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.server.models.Structure;

@Repository
public interface StructureRepository extends JpaRepository<Structure, String> {
  @Query("SELECT structure FROM Structure structure WHERE structure.name = :structureName")
  Optional<Structure> getStructureByName(@Param("structureName") String structureName);
}
