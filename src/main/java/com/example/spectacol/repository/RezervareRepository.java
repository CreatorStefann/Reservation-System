package com.example.spectacol.repository;

import com.example.spectacol.model.Rezervare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RezervareRepository extends JpaRepository<Rezervare, Long> {

    List<Rezervare> findByClientId(Long clientId);
}
