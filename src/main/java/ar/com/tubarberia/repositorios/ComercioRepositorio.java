package ar.com.tubarberia.repositorios;

import ar.com.tubarberia.entidades.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComercioRepositorio extends JpaRepository<Comercio, Long> {

}