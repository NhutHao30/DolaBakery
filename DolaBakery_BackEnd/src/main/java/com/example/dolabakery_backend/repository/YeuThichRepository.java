package com.example.dolabakery_backend.repository;
import com.example.dolabakery_backend.Models.YeuThich;
import com.example.dolabakery_backend.Models.YeuThichId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface YeuThichRepository extends JpaRepository<YeuThich, YeuThichId> {
    List<YeuThich> findByMakh(String makh);
}
