package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.dto.ErrandDTO;
import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.service.ErrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/errands")
public class ErrandController {

    private final ErrandService errandService;

    @Autowired
    public ErrandController(ErrandService errandService) {
        this.errandService = errandService;
    }

    @GetMapping
    public List<Errand> getAllErrands() {
        return errandService.getAllErrands();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Errand> getErrandById(@PathVariable Long id) {
        return errandService.getErrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Errand> createErrand(@ModelAttribute ErrandDTO errandDTO) throws IOException {
        Errand createdErrand = errandService.createErrand(errandDTO);
        return ResponseEntity.ok(createdErrand);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Errand> updateErrand(@PathVariable Long id, @ModelAttribute ErrandDTO errandDTO) throws IOException {
        Errand updatedErrand = errandService.updateErrand(id, errandDTO);
        return ResponseEntity.ok(updatedErrand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteErrand(@PathVariable Long id) {
        errandService.deleteErrand(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{categoryId}")
    public List<Errand> getErrandsByCategory(@PathVariable Long categoryId) {
        return errandService.getErrandsByCategory(categoryId);
    }
}