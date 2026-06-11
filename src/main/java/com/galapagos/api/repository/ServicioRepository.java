package com.galapagos.api.repository;

import com.galapagos.api.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findAllByOrderByIdDesc();

    List<Servicio> findByCategoriaOrderByNombreAsc(String categoria);

    List<Servicio> findByUbicacionContainingIgnoreCaseAndCapacidadMaxPersonaGreaterThanEqual(
            String ubicacion,
            Integer adultos
    );

    List<Servicio> findByCapacidadMaxPersonaGreaterThanEqual(Integer adultos);

    List<Servicio> findByCategoria(String categoria);

    List<Servicio> findByUbicacionContainingIgnoreCase(String ubicacion);

    List<Servicio> findByPrecioBaseLessThanEqual(Double precioMax);

    List<Servicio> findByDestacadoTrueAndCapacidadMaxPersonaGreaterThanEqual(Integer adultos);
}