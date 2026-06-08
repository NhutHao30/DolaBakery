package com.example.dolabakery_backend.service;

import com.example.dolabakery_backend.dto.*;
import com.example.dolabakery_backend.Models.HoaDonBan;
import com.example.dolabakery_backend.repository.HoaDonBanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HoaDonBanServiceImpl implements HoaDonBanService {

    private final HoaDonBanRepository repo;

    private HoaDonBanDTO toDTO(HoaDonBan h) {
        return new HoaDonBanDTO(h.getMaHD(), h.getNgayLap(), h.getMaVanDon(),
                h.getDonViVanChuyen(), h.getGhiChu(), h.getMaKH(), h.getTongTien(), h.getTrangThai());
    }

    @Override
    public PageResponse<HoaDonBanDTO> filterHoaDon(HoaDonBanFilterRequest req) {
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());
        Page<HoaDonBan> page = repo.filterHoaDon(
                req.getMaKH(), req.getTrangThai(), req.getSearch(), req.getTuNgay(), req.getDenNgay(),
                req.getDonViVanChuyen(), req.getTongTienMin(), req.getTongTienMax(),
                pageable);
        return new PageResponse<>(
                page.getContent().stream().map(this::toDTO).toList(),
                page.getNumber(), page.getTotalPages(),
                page.getTotalElements(), page.getSize());
    }

    @Override
    public HoaDonBanDTO getById(String maHD) {
        return toDTO(repo.findById(maHD)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy: " + maHD)));
    }

    @Override
    public HoaDonBanDTO create(HoaDonBanDTO dto) {
        HoaDonBan h = new HoaDonBan(dto.getMaHD(), dto.getNgayLap(), dto.getMaVanDon(),
                dto.getDonViVanChuyen(), dto.getGhiChu(), dto.getMaKH(), dto.getTongTien(), dto.getTrangThai() != null ? dto.getTrangThai() : "Chờ xử lý");
        return toDTO(repo.save(h));
    }

    @Override
    public HoaDonBanDTO update(String maHD, HoaDonBanDTO dto) {
        HoaDonBan h = repo.findById(maHD)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy: " + maHD));
        h.setMaVanDon(dto.getMaVanDon());
        h.setDonViVanChuyen(dto.getDonViVanChuyen());
        h.setGhiChu(dto.getGhiChu());
        h.setMaKH(dto.getMaKH());
        h.setTongTien(dto.getTongTien());
        h.setTrangThai(dto.getTrangThai());
        return toDTO(repo.save(h));
    }

    @Override
    public void delete(String maHD) {
        if (!repo.existsById(maHD))
            throw new RuntimeException("Không tìm thấy: " + maHD);
        repo.deleteById(maHD);
    }
}