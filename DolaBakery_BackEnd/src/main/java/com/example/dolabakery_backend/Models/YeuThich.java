package com.example.dolabakery_backend.Models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "YEUTHICH")
@Data
@NoArgsConstructor
@IdClass(YeuThichId.class)
public class YeuThich {
    @Id
    @Column(name = "MAKH", length = 100)
    private String makh;
    @Id
    @Column(name = "MASP", length = 10)
    private String masp;
    @ManyToOne
    @JoinColumn(name = "MASP", insertable = false, updatable = false)
    private Product product;
}
