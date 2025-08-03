package ifs.ClimaTech.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email; // Usado para login

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoUsuario tipo; // Enum para os papéis

    // Enum para os tipos de usuário
    public enum TipoUsuario {
        COMUM,
        ADM
    }

    // Construtores, Getters e Setters
}
