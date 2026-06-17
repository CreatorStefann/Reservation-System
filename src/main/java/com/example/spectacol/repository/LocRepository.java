package com.example.spectacol.repository;

import com.example.spectacol.model.Loc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocRepository extends JpaRepository<Loc, Long> {

    List<Loc> findBySalaId(Long salaId);
}
