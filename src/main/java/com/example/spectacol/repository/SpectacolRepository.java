package com.example.spectacol.repository;

import com.example.spectacol.model.Spectacol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpectacolRepository extends JpaRepository<Spectacol, Long> {
}
