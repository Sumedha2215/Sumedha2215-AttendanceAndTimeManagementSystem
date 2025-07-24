package com.timeattendance.repository;

import com.timeattendance.model.AttendanceEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceEntryRepository extends MongoRepository<AttendanceEntry, String> {
    List<AttendanceEntry> findByWorkerEmailAndDate(String workerEmail, LocalDate date);
    List<AttendanceEntry> findByWorkerEmail(String email); // ✅ Add this
    long countByWorkerEmailAndInTimeNotNullAndOutTimeNotNull(String email);

    // ✅ New: find all attendance entries for a given date
    List<AttendanceEntry> findByDate(LocalDate date);
}
