package com.galapagos.api.controller;

import com.galapagos.api.model.Comentario;
import com.galapagos.api.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public List<Comentario> listar(@RequestParam(required = false) Long servicioId) {
        return comentarioService.listarPorServicio(servicioId);
    }

    @PostMapping
    public ResponseEntity<?> crearComentario(@Valid @RequestBody Comentario comentario) {
        try {
            Comentario nuevoComentario = comentarioService.guardar(comentario);
            return ResponseEntity.ok(nuevoComentario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comentarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/responder")
    public ResponseEntity<Comentario> responder(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(comentarioService.responder(id, body.get("texto")));
    }
}