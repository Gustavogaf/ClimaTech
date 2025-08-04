package ifs.climatech.repository;

import ifs.climatech.model.Pavilhao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PavilhaoRepository extends JpaRepository<Pavilhao, Long> {
}
