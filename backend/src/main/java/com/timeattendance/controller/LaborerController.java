package com.timeattendance.controller;

import com.timeattendance.model.Laborer;
import com.timeattendance.repository.LaborerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laborers")
@CrossOrigin
public class LaborerController {

    @Autowired
    private LaborerRepository laborerRepository;

    @PostMapping("/add")
    public Laborer addLaborer(@RequestBody Laborer laborer) {
        return laborerRepository.save(laborer);
    }

    @GetMapping("/all")
    public List<Laborer> getAll() {
        return laborerRepository.findAll();
    }

    @GetMapping("/search")
    public List<Laborer> search(@RequestParam String name) {
        return laborerRepository.findByNameContainingIgnoreCase(name);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        laborerRepository.deleteById(id);
    }
}
