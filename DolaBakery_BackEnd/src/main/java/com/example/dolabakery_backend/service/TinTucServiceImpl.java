package com.example.dolabakery_backend.service;

import com.example.dolabakery_backend.dto.TinTucDTO;
import com.example.dolabakery_backend.Models.TinTuc;
import com.example.dolabakery_backend.repository.TinTucRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TinTucServiceImpl implements TinTucService {

    private final TinTucRepository repo;

    private TinTucDTO toDTO(TinTuc t) {
        return new TinTucDTO(t.getMaTinTuc(), t.getHinhAnh(),
                t.getNgayDang(), t.getTieuDe(), t.getMoTa());
    }

    @Override
    public List<TinTucDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public TinTucDTO getById(String maTinTuc) {
        return toDTO(repo.findById(maTinTuc)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy: " + maTinTuc)));
    }

    @Override
    public TinTucDTO create(TinTucDTO dto) {
        TinTuc t = new TinTuc(dto.getMaTinTuc(), dto.getHinhAnh(),
                dto.getNgayDang(), dto.getTieuDe(), dto.getMoTa());
        return toDTO(repo.save(t));
    }

    @Override
    public TinTucDTO update(String maTinTuc, TinTucDTO dto) {
        TinTuc t = repo.findById(maTinTuc)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy: " + maTinTuc));
        t.setHinhAnh(dto.getHinhAnh());
        t.setNgayDang(dto.getNgayDang());
        t.setTieuDe(dto.getTieuDe());
        t.setMoTa(dto.getMoTa());
        return toDTO(repo.save(t));
    }

    @Override
    public void delete(String maTinTuc) {
        if (!repo.existsById(maTinTuc))
            throw new RuntimeException("Không tìm thấy: " + maTinTuc);
        repo.deleteById(maTinTuc);
    }

    @Override
    public List<TinTucDTO> search(String keyword) {
        return repo.findByTieuDeContainingIgnoreCase(keyword)
                .stream().map(this::toDTO).toList();
    }
}