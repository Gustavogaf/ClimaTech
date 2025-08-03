package ifs.ClimaTech.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "equipamentos")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String tag; // Um identificador único, ex: "AC-PV01-S05-01"

    private String marca;

    private String modelo;

    // Muitos equipamentos estão em uma sala
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    // Um equipamento pode ter várias leituras de sensores
    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL)
    private List<LeituraSensor> leituras;
    
    // Construtores, Getters e Setters
}