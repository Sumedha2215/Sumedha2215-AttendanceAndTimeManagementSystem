package com.timeattendance.repository;

import com.timeattendance.model.Laborer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LaborerRepository extends MongoRepository<Laborer, String> {
    List<Laborer> findByNameContainingIgnoreCase(String name);
}
