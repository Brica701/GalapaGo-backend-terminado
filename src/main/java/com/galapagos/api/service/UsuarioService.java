package com.galapagos.api.service;

import com.galapagos.api.model.Usuario;
import com.galapagos.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario login(String email, String password) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("El correo electrónico no está registrado."));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("La contraseña es incorrecta.");
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario guardar(Usuario usuario) {

        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new RuntimeException("La contraseña es obligatoria para nuevos usuarios.");
        }

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER");
        }

        return repository.save(usuario);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: el usuario no existe.");
        }
        repository.deleteById(id);
    }

    public Usuario actualizar(Long id, Usuario nuevo) {
        return repository.findById(id).map(u -> {
            u.setNombre(nuevo.getNombre());
            u.setApellidos(nuevo.getApellidos());
            u.setEmail(nuevo.getEmail());

            if (nuevo.getRol() != null) {
                u.setRol(nuevo.getRol());
            }

            if (nuevo.getPassword() != null && !nuevo.getPassword().isEmpty()) {
                u.setPassword(nuevo.getPassword());
            }

            return repository.save(u);
        }).orElseThrow(() -> new RuntimeException("No se pudo actualizar: el usuario no existe."));
    }
}