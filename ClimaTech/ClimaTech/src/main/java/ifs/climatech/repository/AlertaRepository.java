package ifs.climatech.repository;

import ifs.climatech.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    // Método para buscar um alerta pelo seu código único
    Optional<Alerta> findByCodigoAlerta(String codigoAlerta);
}
