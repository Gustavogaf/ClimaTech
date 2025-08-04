package ifs.climatech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifs.climatech.model.Equipamento;

import java.util.Optional;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    // Exemplo de um método de busca personalizado que será muito útil
    Optional<Equipamento> findByTag(String tag);
}
