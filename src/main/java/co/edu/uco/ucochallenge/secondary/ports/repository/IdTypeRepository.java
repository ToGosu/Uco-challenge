package co.edu.uco.ucochallenge.secondary.ports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;

@Repository
public interface IdTypeRepository extends JpaRepository<IdTypeEntity, UUID> {
    List<IdTypeEntity> findAll();
    Optional<IdTypeEntity> findById(UUID id);
}
