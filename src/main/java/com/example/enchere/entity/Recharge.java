package com.example.enchere.entity;

import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "compte")
@Getter
public class Recharge {

    @Id
    @Column
    private int id_compte;

    @Column(name = "id_membre")
    private int membre;

    @Column
    private double solde;

    @Column
    private LocalDate dates;

    @Column
    private int etat;
}
