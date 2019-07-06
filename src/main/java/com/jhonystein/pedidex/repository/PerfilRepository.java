package com.jhonystein.pedidex.repository;

import com.jhonystein.pedidex.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>{
    Perfil findByNome(String nome);
}
