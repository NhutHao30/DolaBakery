package com.example.dolabakery_backend.controller;

import com.example.dolabakery_backend.dto.TinTucDTO;
import com.example.dolabakery_backend.service.TinTucService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tin-tuc")
@RequiredArgsConstructor
public class TinTucController {

    private final TinTucService service;

    @GetMapping
    public ResponseEntity<List<TinTucDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TinTucDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(service.search(keyword));
    }

    @GetMapping("/{maTinTuc}")
    public ResponseEntity<TinTucDTO> getById(@PathVariable String maTinTuc) {
        return ResponseEntity.ok(service.getById(maTinTuc));
    }

    @PostMapping
    public ResponseEntity<TinTucDTO> create(@RequestBody TinTucDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{maTinTuc}")
    public ResponseEntity<TinTucDTO> update(@PathVariable String maTinTuc,
                                            @RequestBody TinTucDTO dto) {
        return ResponseEntity.ok(service.update(maTinTuc, dto));
    }

    @DeleteMapping("/{maTinTuc}")
    public ResponseEntity<String> delete(@PathVariable String maTinTuc) {
        service.delete(maTinTuc);
        return ResponseEntity.ok("Đã xóa: " + maTinTuc);
    }
}