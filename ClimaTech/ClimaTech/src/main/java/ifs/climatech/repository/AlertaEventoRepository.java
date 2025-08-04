package ifs.climatech.repository;

import ifs.climatech.model.AlertaEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaEventoRepository extends JpaRepository<AlertaEvento, Long> {
}