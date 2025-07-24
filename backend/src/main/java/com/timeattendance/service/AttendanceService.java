package com.timeattendance.service;

import com.timeattendance.model.AttendanceEntry;
import com.timeattendance.repository.AttendanceEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    private static final ZoneId IST_ZONE = ZoneId.of("Asia/Kolkata");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public String markEntry(String email, String action, LocalDate date) {
        try {
            ZonedDateTime nowIST = ZonedDateTime.now(IST_ZONE);
            String currentTime = nowIST.format(TIME_FORMATTER);

            AttendanceEntry newEntry = new AttendanceEntry();
            newEntry.setWorkerEmail(email);
            newEntry.setDate(date);

            if ("IN".equalsIgnoreCase(action)) {
                newEntry.setInTime(currentTime);
            } else if ("OUT".equalsIgnoreCase(action)) {
                newEntry.setOutTime(currentTime);
            } else {
                return "Invalid action";
            }

            attendanceEntryRepository.save(newEntry);
            return "Attendance marked: " + action;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error marking attendance: " + e.getMessage();
        }
    }

    public long countPresentDays(String email) {
        List<AttendanceEntry> entries = attendanceEntryRepository.findAll();
        return entries.stream()
                .filter(e -> email.equalsIgnoreCase(e.getWorkerEmail()) && e.getInTime() != null && e.getOutTime() != null)
                .map(e -> e.getDate().toString())
                .distinct()
                .count();
    }

    public double calculateAttendancePercentage(String email) {
        long presentDays = countPresentDays(email);
        int contractDays = 30;
        return contractDays == 0 ? 0.0 : (presentDays * 100.0) / contractDays;
    }

    public String unmarkEntry(String email, String action, LocalDate date) {
        List<AttendanceEntry> entries = attendanceEntryRepository.findByWorkerEmailAndDate(email, date);

        if (!entries.isEmpty()) {
            AttendanceEntry latestEntry = entries.get(entries.size() - 1);
            if ("IN".equalsIgnoreCase(action)) {
                latestEntry.setInTime(null);
            } else if ("OUT".equalsIgnoreCase(action)) {
                latestEntry.setOutTime(null);
            } else {
                return "Invalid action";
            }
            attendanceEntryRepository.save(latestEntry);
            return "Attendance " + action + " removed successfully";
        } else {
            return "No attendance record found for selected date";
        }
    }
}
