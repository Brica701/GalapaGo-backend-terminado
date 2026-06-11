package com.galapagos.api.service;

import com.galapagos.api.model.Comentario;
import com.galapagos.api.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicioService servicioService;

    public Comentario guardar(Comentario comentario) {

        var usuario = usuarioService.obtenerPorId(comentario.getUsuario().getId());

        var servicio = servicioService.obtenerPorId(comentario.getServicio().getId());

        if (comentario.getPuntuacion() == null || comentario.getPuntuacion() < 1 || comentario.getPuntuacion() > 5) {
            throw new RuntimeException("La puntuación es obligatoria y debe estar entre 1 y 5.");
        }

        comentario.setUsuario(usuario);
        comentario.setServicio(servicio);

        comentario.setFechaPublicacion(LocalDateTime.now());

        return comentarioRepository.save(comentario);
    }

    public List<Comentario> listarPorServicio(Long servicioId) {
        if (servicioId != null) {

            return comentarioRepository.findByServicioIdOrderByFechaPublicacionDesc(servicioId);
        }

        return comentarioRepository.findAllByOrderByFechaPublicacionDesc();
    }

    public void eliminar(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: el comentario no existe.");
        }
        comentarioRepository.deleteById(id);
    }

    public Comentario responder(Long id, String textoRespuesta) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        comentario.setRespuestaAdmin(textoRespuesta);
        comentario.setFechaRespuesta(LocalDateTime.now());
        return comentarioRepository.save(comentario);
    }
}