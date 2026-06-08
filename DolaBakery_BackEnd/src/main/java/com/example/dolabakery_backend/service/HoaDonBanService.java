package com.example.dolabakery_backend.service;

import com.example.dolabakery_backend.dto.*;

public interface HoaDonBanService {
    PageResponse<HoaDonBanDTO> filterHoaDon(HoaDonBanFilterRequest request);
    HoaDonBanDTO getById(String maHD);
    HoaDonBanDTO create(HoaDonBanDTO dto);
    HoaDonBanDTO update(String maHD, HoaDonBanDTO dto);
    void delete(String maHD);
}