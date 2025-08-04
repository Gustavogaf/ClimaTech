package ifs.climatech.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifs.climatech.model.Equipamento;
import ifs.climatech.model.LeituraSensor;

import java.util.List;

@Repository
public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {

    // Busca as últimas 'N' leituras para um equipamento específico, ordenadas pela mais recente.
    List<LeituraSensor> findByEquipamentoOrderByTimestampDesc(Equipamento equipamento, Pageable pageable);

}
