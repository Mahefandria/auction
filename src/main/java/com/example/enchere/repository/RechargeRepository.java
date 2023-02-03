package com.example.enchere.repository;

import com.example.enchere.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Integer> {
    List<Recharge> findAllByMembreOrderByDatesDesc(int membreId);
}
