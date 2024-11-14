package com.projectdemo1.repository;

import com.projectdemo1.domain.boardContent.color.PetColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetColorRepository extends JpaRepository<PetColor, Long> {
    Optional<PetColor> findByColor(String color);
}
