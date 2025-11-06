package co.edu.uco.ucochallenge.secondary.ports.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	boolean existsByIdNumber(String idNumber);
	boolean existsByEmail(String email);
	boolean existsByMobileNumber(String mobileNumber);
}
