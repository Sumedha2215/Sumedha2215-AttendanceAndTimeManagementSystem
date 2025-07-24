package com.timeattendance.controller;

import com.timeattendance.dto.ContractUpdateDTO;
import com.timeattendance.model.AttendanceEntry;
import com.timeattendance.model.Worker;
import com.timeattendance.repository.WorkerRepository;
import com.timeattendance.repository.AttendanceEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/workers")
@CrossOrigin
public class WorkerController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    // ✅ Register
   @PostMapping("/register")
public ResponseEntity<String> registerWorker(@RequestBody Worker worker) {
    if (worker == null ||
        worker.getEmail() == null ||
        worker.getPassword() == null ||
        worker.getMobileNumber() == null ||   // <-- ✅ REQUIRED
        worker.getIdNumber() == null) {
        return ResponseEntity.badRequest().body("Missing required fields");
    }

    String email = worker.getEmail().trim().toLowerCase();
    if (workerRepository.findByEmailIgnoreCase(email) != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
    }

    worker.setEmail(email);
    worker.setRole("worker");

    // ✅ Double-check this: Maybe mobileNumber is not set in frontend before saving?
    workerRepository.save(worker);
    return ResponseEntity.ok("Worker registered successfully");
}


    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<String> loginWorker(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Missing credentials");
        }

        Worker worker = workerRepository.findByEmailIgnoreCase(email.trim().toLowerCase());
        if (worker != null && worker.getPassword().trim().equals(password.trim())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    // ✅ Get worker by email
    @GetMapping("/{email:.+}")
    public ResponseEntity<Worker> getWorkerProfile(@PathVariable String email) {
        Worker worker = workerRepository.findByEmailIgnoreCase(email.trim().toLowerCase());
        return (worker != null) ? ResponseEntity.ok(worker) : ResponseEntity.notFound().build();
    }

    // ✅ Get all workers
    @GetMapping("")
    public ResponseEntity<?> getAllWorkers() {
        try {
            return ResponseEntity.ok(workerRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load workers");
        }
    }

    // ✅ Update Contract Days (by email)
    @PutMapping("/update-contract-days/{email}")
    public ResponseEntity<String> updateContractDays(
        @PathVariable String email,
        @RequestBody ContractUpdateDTO request
    ) {
        Worker worker = workerRepository.findByEmailIgnoreCase(email.trim().toLowerCase());
        if (worker != null) {
            worker.setContractDays(request.getContractDays());
            workerRepository.save(worker);
            return ResponseEntity.ok("Contract days updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found");
        }
    }

    // ✅ Update Worker Type (by ID)
    @PutMapping("/{id}")
    public ResponseEntity<String> updateWorkerType(
        @PathVariable String id,
        @RequestBody Map<String, Object> updates
    ) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found");
        }

        Worker worker = optionalWorker.get();

        if (updates.containsKey("workerType")) {
            worker.setWorkerType((String) updates.get("workerType"));
        }

        workerRepository.save(worker);
        return ResponseEntity.ok("Worker type updated successfully");
    }

   // ✅ Get all workers with attendance (present/contract)
@GetMapping("/with-attendance")
public ResponseEntity<?> getAllWorkersWithAttendance() {
    try {
        List<Worker> workers = workerRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Worker worker : workers) {
            List<AttendanceEntry> allEntries = attendanceEntryRepository.findByWorkerEmail(worker.getEmail());

            Map<LocalDate, List<AttendanceEntry>> groupedByDate = new HashMap<>();
            for (AttendanceEntry entry : allEntries) {
                groupedByDate.computeIfAbsent(entry.getDate(), d -> new ArrayList<>()).add(entry);
            }

            long presentDays = groupedByDate.values().stream()
                .filter(entries ->
                    entries.stream().anyMatch(e -> e.getInTime() != null) &&
                    entries.stream().anyMatch(e -> e.getOutTime() != null))
                .count();

            int contractDays = (worker.getContractDays() != null) ? worker.getContractDays() : 0;

            Map<String, Object> map = new HashMap<>();
            map.put("idNumber", worker.getIdNumber());
            map.put("name", worker.getName());
            map.put("email", worker.getEmail());
            map.put("workerType", worker.getWorkerType());
            map.put("contractDays", worker.getContractDays());
            map.put("shift", worker.getShift());
            map.put("attendance", presentDays + "/" + contractDays);

            result.add(map);
        }

        return ResponseEntity.ok(result);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading attendance data");
    }
}
}
