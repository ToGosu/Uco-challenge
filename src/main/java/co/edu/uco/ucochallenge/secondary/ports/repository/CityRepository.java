package co.edu.uco.ucochallenge.secondary.ports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, UUID> {
    List<CityEntity> findAll();
    Optional<CityEntity> findById(UUID id);
    List<CityEntity> findByStateId(UUID stateId);
}