package ifs.ClimaTech.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leituras_sensores")
public class LeituraSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp; // Data e hora da leitura

    private double amperagem;
    private double voltagem;
    private double temperatura;

    // Muitas leituras pertencem a um equipamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    // Construtores, Getters e Setters
}