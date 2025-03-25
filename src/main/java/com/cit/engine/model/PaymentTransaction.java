package com.cit.engine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    @JsonFormat(pattern = "yyyy-MM-dd") // Ensures correct date format

    private LocalDate settlementDate;
    @JsonFormat(pattern = "yyyy-MM-dd") // Ensures correct date format

    private LocalDate valueDate;
    private Boolean inbound;
}

