package com.sistema.contable.controller;

import com.sistema.contable.model.Usuario;
import com.sistema.contable.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Función de encriptación SHA-256 irreversible
    private String encriptarSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar contraseña", e);
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "").trim().toLowerCase();
        String nombre = body.getOrDefault("nombre", "").trim();
        String password = body.getOrDefault("password", "");

        if (username.length() < 3 || nombre.length() < 3 || password.length() < 4) {
            return ResponseEntity.badRequest().body(Map.of("error", "Completa todos los campos (contraseña mínimo 4 caracteres)."));
        }
        if (usuarioRepository.existsByUsernameIgnoreCase(username)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ese nombre de usuario ya está registrado."));
        }

        Usuario nuevo = new Usuario();
        nuevo.setUsername(username);
        nuevo.setNombre(nombre);
        nuevo.setPasswordHash(encriptarSHA256(password));

        Usuario guardado = usuarioRepository.save(nuevo);
        return ResponseEntity.ok(Map.of(
            "id", guardado.getId(),
            "username", guardado.getUsername(),
            "nombre", guardado.getNombre()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.getOrDefault("username", "").trim().toLowerCase();
        String password = body.getOrDefault("password", "");

        Optional<Usuario> opt = usuarioRepository.findByUsernameIgnoreCase(username);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario o contraseña incorrectos."));
        }

        Usuario u = opt.get();
        String hashIngresado = encriptarSHA256(password);
        if (!u.getPasswordHash().equals(hashIngresado)) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario o contraseña incorrectos."));
        }

        return ResponseEntity.ok(Map.of(
            "id", u.getId(),
            "username", u.getUsername(),
            "nombre", u.getNombre()
        ));
    }
}