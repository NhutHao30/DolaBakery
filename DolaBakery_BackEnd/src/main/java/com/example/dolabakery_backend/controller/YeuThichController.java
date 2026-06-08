package com.example.dolabakery_backend.controller;
import com.example.dolabakery_backend.Models.KhachHang;
import com.example.dolabakery_backend.Models.Product;
import com.example.dolabakery_backend.Models.YeuThich;
import com.example.dolabakery_backend.Models.YeuThichId;
import com.example.dolabakery_backend.dto.WishlistRequest;
import com.example.dolabakery_backend.repository.KhachHangRepository;
import com.example.dolabakery_backend.repository.ProductRepository;
import com.example.dolabakery_backend.repository.YeuThichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.example.dolabakery_backend.Models.TaiKhoan;
import java.util.List;
@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class YeuThichController {
    @Autowired
    private YeuThichRepository yeuThichRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    private String getMaKhachHang(HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("user");
        if (tk == null) return null;
        KhachHang kh = khachHangRepository.findByUserName(tk.getUserName());
        return (kh != null) ? kh.getMaKH() : null;
    }

    @GetMapping
    public ResponseEntity<?> getWishlist(HttpSession session) {
        String makh = getMaKhachHang(session);
        if (makh == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");
        return ResponseEntity.ok(yeuThichRepository.findByMakh(makh));
    }
    @PostMapping
    public ResponseEntity<?> addToWishlist(@RequestBody WishlistRequest request, HttpSession session) {
        String makh = getMaKhachHang(session);
        if (makh == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");

        Product product = productRepository.findById(request.getMasp()).orElse(null);
        if (product == null) return ResponseEntity.badRequest().body("{\"error\":\"Product not found\"}");
        
        YeuThich yeuThich = new YeuThich();
        yeuThich.setMakh(makh);
        yeuThich.setMasp(request.getMasp());
        if (yeuThichRepository.existsById(new YeuThichId(makh, request.getMasp()))) {
            return ResponseEntity.badRequest().body("Already in wishlist");
        }
        yeuThichRepository.save(yeuThich);
        return ResponseEntity.ok(yeuThich);
    }
    @DeleteMapping("/{masp}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable String masp, HttpSession session) {
        String makh = getMaKhachHang(session);
        if (makh == null) return ResponseEntity.status(401).body("{\"error\":\"Chưa đăng nhập\"}");

        YeuThichId id = new YeuThichId(makh, masp);
        if (yeuThichRepository.existsById(id)) {
            yeuThichRepository.deleteById(id);
            return ResponseEntity.ok().body("{\"message\":\"Removed\"}");
        }
        return ResponseEntity.badRequest().body("{\"message\":\"Not found in wishlist\"}");
    }
}
