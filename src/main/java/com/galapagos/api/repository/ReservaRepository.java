package com.galapagos.api.repository;

import com.galapagos.api.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);

    long count();

    @Query("SELECT COALESCE(SUM(r.precioTotal), 0.0) FROM Reserva r WHERE r.estado = 'CONFIRMADA'")
    Double sumTotalIngresos();

    @Query("SELECT COALESCE(SUM(r.cantidadHabitaciones), 0) FROM Reserva r " +
            "WHERE r.habitacion.id = :habitacionId " +
            "AND r.estado = 'CONFIRMADA' " +
            "AND (:fechaInicio < r.fechaFin AND :fechaFin > r.fechaInicio)")

    Integer contarHabitacionesReservadasEnFechas(
            @Param("habitacionId") Long habitacionId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}