package ar.com.tubarberia.repositorios;

import ar.com.tubarberia.entidades.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioRepositorio extends JpaRepository<Calendario, Long> {
}
