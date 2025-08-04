package ifs.climatech.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigoAlerta; // Ex: "FALHA_TENSAO", "CONSUMO_ELEVADO"

    @Column(nullable = false)
    private String descricao;
    
    private boolean ativo = true;

    // Construtores, Getters e Setters
}
