package com.galapagos.api.controller;

import com.galapagos.api.model.Reserva;
import com.galapagos.api.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Reserva> listarPorUsuario(@PathVariable Long usuarioId) {
        return reservaService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(reservaService.obtenerEstadisticasDashboard());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.crearReserva(reserva);
            return ResponseEntity.ok(nuevaReserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Reserva> confirmarReserva(@PathVariable Long id) {
        Reserva reservaActualizada = reservaService.confirmarReserva(id);
        return ResponseEntity.ok(reservaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }
}