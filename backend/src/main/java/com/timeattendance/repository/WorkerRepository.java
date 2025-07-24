package com.timeattendance.repository;

import com.timeattendance.model.Worker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkerRepository extends MongoRepository<Worker, String> {
    Worker findByEmailIgnoreCase(String email);
    Worker findByMobileNumber(String mobileNumber);
    Worker findByIdNumber(String idNumber);
    
}
