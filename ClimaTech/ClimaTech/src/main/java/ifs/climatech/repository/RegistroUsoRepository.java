package ifs.climatech.repository;

import ifs.climatech.model.Equipamento;
import ifs.climatech.model.RegistroUso;
import ifs.climatech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroUsoRepository extends JpaRepository<RegistroUso, Long> {

    // Encontra o último registro de uso para um equipamento que ainda está ativo (não tem data de fim)
    Optional<RegistroUso> findByEquipamentoAndDataHoraFimIsNull(Equipamento equipamento);

    // Encontra todos os registros de uso de um usuário específico
    List<RegistroUso> findByUsuario(Usuario usuario);
}
