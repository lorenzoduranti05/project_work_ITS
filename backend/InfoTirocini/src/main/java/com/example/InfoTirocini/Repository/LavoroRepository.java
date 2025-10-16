package com.example.InfoTirocini.Repository;

import com.example.InfoTirocini.Model.Lavoro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LavoroRepository extends JpaRepository<Lavoro, Integer> {
}