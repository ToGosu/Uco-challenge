package co.edu.uco.ucochallenge.primary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/uco-challenge")
public class RootController {

    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> getRoot() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "UCO Challenge API");
        response.put("version", "0.0.1-SNAPSHOT");
        response.put("status", "running");
        response.put("endpoints", Map.of(
            "health", "/uco-challenge/api/v1/health",
            "cities", "/uco-challenge/api/v1/cities",
            "idtypes", "/uco-challenge/api/v1/idtypes",
            "users", "/uco-challenge/api/v1/users",
            "actuator", "/actuator/health"
        ));
        return ResponseEntity.ok(response);
    }
}

