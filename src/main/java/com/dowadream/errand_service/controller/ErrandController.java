package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.dto.ErrandDTO;
import com.dowadream.errand_service.service.ErrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/errandservice/errands")
public class ErrandController {

    private final ErrandService errandService;

    @Autowired
    public ErrandController(ErrandService errandService) {
        this.errandService = errandService;
    }

    @GetMapping
    public Page<ErrandDTO> getAllErrands(Pageable pageable) {
        return errandService.getAllErrands(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ErrandDTO> getErrandById(@PathVariable Long id) {
        return errandService.getErrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ErrandDTO> createErrand(@ModelAttribute ErrandDTO errandDTO) {
        try {
            ErrandDTO createdErrand = errandService.createErrand(errandDTO);
            return ResponseEntity.ok(createdErrand);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ErrandDTO> updateErrand(@PathVariable Long id, @ModelAttribute ErrandDTO errandDTO) {
        try {
            ErrandDTO updatedErrand = errandService.updateErrand(id, errandDTO);
            return ResponseEntity.ok(updatedErrand);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteErrand(@PathVariable Long id) {
        errandService.deleteErrand(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{categoryId}")
    public Page<ErrandDTO> getErrandsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return errandService.getErrandsByCategory(categoryId, pageable);
    }
}