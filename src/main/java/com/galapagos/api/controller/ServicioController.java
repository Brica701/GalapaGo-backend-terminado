package com.galapagos.api.controller;

import com.galapagos.api.model.Servicio;
import com.galapagos.api.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "http://localhost:4200")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public List<Servicio> listar() {
        return servicioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    public List<Servicio> buscar(
            @RequestParam(required = false) String ubicacion,
            @RequestParam(defaultValue = "1") Integer adultos) {

        return servicioService.buscarConFiltros(ubicacion, adultos);
    }

    @PostMapping
    public Servicio crear(@RequestBody Servicio servicio) {
        normalizarAmenities(servicio);
        return servicioService.guardar(servicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        normalizarAmenities(servicio);
        return ResponseEntity.ok(servicioService.actualizar(id, servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private void normalizarAmenities(Servicio servicio) {
        if (servicio.getTieneWifi() == null) servicio.setTieneWifi(false);
        if (servicio.getTieneDesayuno() == null) servicio.setTieneDesayuno(false);
        if (servicio.getTieneTransporte() == null) servicio.setTieneTransporte(false);
        if (servicio.getTieneEquipo() == null) servicio.setTieneEquipo(false);
    }
}