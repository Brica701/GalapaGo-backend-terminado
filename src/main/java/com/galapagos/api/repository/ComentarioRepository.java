package com.galapagos.api.repository;

import com.galapagos.api.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByServicioIdOrderByFechaPublicacionDesc(Long servicioId);

    List<Comentario> findAllByOrderByFechaPublicacionDesc();
}