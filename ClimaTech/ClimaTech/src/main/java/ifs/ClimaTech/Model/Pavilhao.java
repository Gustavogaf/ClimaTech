package ifs.ClimaTech.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pavilhoes")
public class Pavilhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    // Um pavilhão pode ter várias salas
    @OneToMany(mappedBy = "pavilhao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sala> salas;

    // Construtores, Getters e Setters
}