package com.example.dolabakery_backend.service;

import com.example.dolabakery_backend.dto.TinTucDTO;
import java.util.List;

public interface TinTucService {
    List<TinTucDTO> getAll();
    TinTucDTO getById(String maTinTuc);
    TinTucDTO create(TinTucDTO dto);
    TinTucDTO update(String maTinTuc, TinTucDTO dto);
    void delete(String maTinTuc);
    List<TinTucDTO> search(String keyword);
}