package ifs.ClimaTech.Repository;

import ifs.ClimaTech.Model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    // Métodos de busca personalizados podem ser adicionados aqui no futuro.
}
