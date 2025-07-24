package com.timeattendance.controller;

import com.timeattendance.dto.LoginRequest;
import com.timeattendance.dto.WorkerRegistrationRequest;
import com.timeattendance.model.Worker;
import com.timeattendance.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private WorkerRepository workerRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";
        String role = request.getRole() != null ? request.getRole().trim().toLowerCase() : "";

        System.out.println("Login Attempt => Email: " + email + ", Role: " + role);

        if ("admin".equals(role)) {
            if (email.equals("admin@example.com") && password.equals("admin123")) {
                System.out.println("Returning redirect: /admin/dashboard.html");
                return ResponseEntity.ok("/admin/dashboard.html");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin credentials");
            }
        }

        if ("manager".equals(role)) {
            if ((email.equals("manager1@example.com") && password.equals("manager123")) ||
                (email.equals("manager2@example.com") && password.equals("manager456"))) {
                System.out.println("Returning redirect: /manager-profile.html");
                return ResponseEntity.ok("/manager-profile.html");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid manager credentials");
            }
        }

        if ("worker".equals(role)) {
            Worker worker = workerRepository.findByEmailIgnoreCase(email);
            if (worker != null) {
                if (worker.getPassword().trim().equals(password)) {
                    return ResponseEntity.ok("worker");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid worker credentials");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid worker credentials");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid role or credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerWorker(@RequestBody WorkerRegistrationRequest request) {
        String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";

        if (workerRepository.findByEmailIgnoreCase(email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Worker with this email already exists.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match.");
        }

        Worker worker = new Worker();
        worker.setName(request.getName());
        worker.setEmail(email);
        worker.setPassword(request.getPassword().trim());
        worker.setMobileNumber(request.getMobileNumber());
        worker.setAddress(request.getAddress());
        worker.setIdNumber(request.getIdNumber());
        worker.setRole("worker");
        worker.setShift(request.getShift());
        worker.setJoiningDate(request.getJoiningDate());

        workerRepository.save(worker);
        return ResponseEntity.ok("Worker registered successfully.");
    }

    @GetMapping("/workers/profile")
    public ResponseEntity<?> getWorkerProfile(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }

        Worker worker = workerRepository.findByEmailIgnoreCase(email.trim().toLowerCase());
        if (worker == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found");
        }

        return ResponseEntity.ok(worker);
    }
}
