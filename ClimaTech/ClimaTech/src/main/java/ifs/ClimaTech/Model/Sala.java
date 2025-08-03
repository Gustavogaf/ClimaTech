package ifs.ClimaTech.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome; // Ex: "Sala 05", "Laboratório de Química"

    // Muitas salas pertencem a um pavilhão
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pavilhao_id", nullable = false)
    private Pavilhao pavilhao;

    // Uma sala pode ter vários equipamentos
    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Equipamento> equipamentos;

    // Construtores, Getters e Setters
}
