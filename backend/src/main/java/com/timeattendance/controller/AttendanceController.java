package com.timeattendance.controller;

import com.timeattendance.model.AttendanceEntry;
import com.timeattendance.model.Worker;
import com.timeattendance.repository.AttendanceEntryRepository;
import com.timeattendance.repository.WorkerRepository;
import com.timeattendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    // ✅ Final method: In/Out based attendance marking
    @PostMapping("/mark-entry")
public ResponseEntity<String> markInOut(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    String action = request.get("action");
    String dateStr = request.get("date");

    if (email == null || action == null || dateStr == null) {
        return ResponseEntity.badRequest().body("Missing email, action, or date");
    }

    LocalDate date = LocalDate.parse(dateStr);
    String result = attendanceService.markEntry(email.trim().toLowerCase(), action.trim().toUpperCase(), date);
    return ResponseEntity.ok(result);
}

    // Optional: attendance percentage
    @GetMapping("/percentage/{email}")
    public double getAttendancePercentage(@PathVariable String email) {
        return attendanceService.calculateAttendancePercentage(email);
    }

    // ✅ New: Fetch attendance entries by date
 @GetMapping("/entries")
public List<AttendanceEntry> getEntriesByDate(@RequestParam String date) {
    LocalDate localDate = LocalDate.parse(date);
    return attendanceEntryRepository.findByDate(localDate);
}
@PostMapping("/unmark-entry")
public ResponseEntity<String> unmarkInOut(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    String action = request.get("action");
    String dateStr = request.get("date");

    if (email == null || action == null || dateStr == null) {
        return ResponseEntity.badRequest().body("Missing email, action, or date");
    }

    LocalDate date = LocalDate.parse(dateStr);
    String result = attendanceService.unmarkEntry(email.trim().toLowerCase(), action.trim().toUpperCase(), date);
    return ResponseEntity.ok(result);
}
@GetMapping("/summary/{email}")
public ResponseEntity<Map<String, Object>> getWorkerAttendanceSummary(@PathVariable String email) {
    Worker worker = workerRepository.findByEmailIgnoreCase(email.trim().toLowerCase());
    if (worker == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Worker not found"));
    }

    List<AttendanceEntry> entries = attendanceEntryRepository.findByWorkerEmail(email);
    Map<LocalDate, List<AttendanceEntry>> grouped = new HashMap<>();
    for (AttendanceEntry entry : entries) {
        grouped.computeIfAbsent(entry.getDate(), d -> new ArrayList<>()).add(entry);
    }

    long presentDays = grouped.values().stream()
        .filter(list ->
            list.stream().anyMatch(e -> e.getInTime() != null) &&
            list.stream().anyMatch(e -> e.getOutTime() != null)
        )
        .count();

    // 🧠 Total expected days = from joining date to today
    LocalDate today = LocalDate.now();
    LocalDate joiningDate = LocalDate.parse(worker.getJoiningDate());

    long totalExpectedDays = joiningDate.until(today).getDays() + 1; // +1 to include today

    Map<String, Object> response = new HashMap<>();
    response.put("presentDays", presentDays);
    response.put("totalDays", totalExpectedDays);

    return ResponseEntity.ok(response);
}



}
