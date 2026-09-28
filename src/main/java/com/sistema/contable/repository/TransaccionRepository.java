package com.sistema.contable.repository;

import com.sistema.contable.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
java.util.List<Transaccion> findByUsuarioIdOrderByIdAsc(Long usuarioId);
}