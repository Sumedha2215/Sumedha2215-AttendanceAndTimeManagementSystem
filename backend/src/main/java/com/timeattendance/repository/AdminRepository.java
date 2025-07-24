package com.timeattendance.repository;

import com.timeattendance.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findByUsernameAndPasswordAndShift(String username, String password, String shift);
}
