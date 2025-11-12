package co.edu.uco.ucochallenge.primary.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.application.Response;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.ConfirmEmailInteractor;
import co.edu.uco.ucochallenge.user.confirmuser.application.interactor.ConfirmMobileInteractor;
import co.edu.uco.ucochallenge.user.getusers.application.interactor.GetUsersInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.UserOutputDTO;

@RestController
@RequestMapping({"/uco-challenge/api/v1/users", "/uco-challenge/v1/users"})
public class UserController {
	
	private final RegisterUserInteractor registerUserInteractor;
	private final GetUsersInteractor getUsersInteractor;
	private final ConfirmEmailInteractor confirmEmailInteractor;
	private final ConfirmMobileInteractor confirmMobileInteractor;
	
	public UserController(
			RegisterUserInteractor registerUserInteractor,
			GetUsersInteractor getUsersInteractor,
			ConfirmEmailInteractor confirmEmailInteractor,
			ConfirmMobileInteractor confirmMobileInteractor) {
		this.registerUserInteractor = registerUserInteractor;
		this.getUsersInteractor = getUsersInteractor;
		this.confirmEmailInteractor = confirmEmailInteractor;
		this.confirmMobileInteractor = confirmMobileInteractor;
	}
	
	@PostMapping
	public ResponseEntity<String> registerUser(@RequestBody RegisterUserInputDTO dto) {
		var message = "User registered successfully!";
		var normalizedDto = RegisterUserInputDTO.normalize(
			dto.idType(),
			dto.idNumber(),
			dto.firstName(),
			dto.secondName(),
			dto.firstSurname(),
			dto.secondSurname(),
			dto.homeCity(),
			dto.email(),
			dto.mobileNumber()
		);
		registerUserInteractor.execute(normalizedDto);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<UserOutputDTO>> getAllUsers() {
		Response<List<UserOutputDTO>> response = getUsersInteractor.execute(null);
		
		if (response.isDataReturned()) {
			return ResponseEntity.ok(response.getData());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/confirm-email")
	public ResponseEntity<String> confirmEmail(@RequestParam String token) {
		try {
			confirmEmailInteractor.execute(token);
			return ResponseEntity.ok("Correo electrónico confirmado exitosamente");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al confirmar correo: " + e.getMessage());
		}
	}
	
	@PostMapping("/confirm-mobile")
	public ResponseEntity<String> confirmMobile(@RequestParam String code) {
		try {
			confirmMobileInteractor.execute(code);
			return ResponseEntity.ok("Número de celular confirmado exitosamente");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al confirmar celular: " + e.getMessage());
		}
	}
}
