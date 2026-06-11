package com.galapagos.api.repository;

import com.galapagos.api.model.DetalleServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleServicioRepository extends JpaRepository<DetalleServicio, Long> {
}