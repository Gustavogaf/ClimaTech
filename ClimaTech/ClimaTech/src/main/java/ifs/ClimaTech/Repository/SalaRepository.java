package ifs.climatech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifs.climatech.model.Pavilhao;
import ifs.climatech.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    boolean existsByNomeAndPavilhao(String nome, Pavilhao pavilhao);
}
