package com.example.spectacol.repository;

import com.example.spectacol.model.RezervareLoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RezervareLocRepository extends JpaRepository<RezervareLoc, Long> {

    List<RezervareLoc> findByLocId(Long locId);
}
