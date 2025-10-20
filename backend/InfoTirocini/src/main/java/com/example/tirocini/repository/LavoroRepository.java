package com.example.tirocini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tirocini.model.Lavoro;

@Repository
public interface LavoroRepository extends JpaRepository<Lavoro, Integer> {
}