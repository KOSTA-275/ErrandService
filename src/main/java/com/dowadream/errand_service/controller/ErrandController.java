package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.service.ErrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{errandSeq}")
    public ResponseEntity<Errand> getErrandById(@PathVariable Long errandSeq) {
        return errandService.getErrandById(errandSeq)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Errand createErrand(@RequestBody Errand errand) {
        return errandService.createErrand(errand);
    }

    @PutMapping("/{errandSeq}")
    public ResponseEntity<Errand> updateErrand(@PathVariable Long errandSeq, @RequestBody Errand errandDetails) {
        Errand updatedErrand = errandService.updateErrand(errandSeq, errandDetails);
        return updatedErrand != null ? ResponseEntity.ok(updatedErrand) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{errandSeq}")
    public ResponseEntity<Void> deleteErrand(@PathVariable Long errandSeq) {
        errandService.deleteErrand(errandSeq);
        return ResponseEntity.ok().build();
    }
}