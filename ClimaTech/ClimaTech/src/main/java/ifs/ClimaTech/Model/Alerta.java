package ifs.climatech.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigoAlerta;

    @Column(nullable = false)
    private String descricao;
    
    // --- NOVO CAMPO ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAlerta tipo;
    // --- FIM DO NOVO CAMPO ---

    private boolean ativo = true;

    // Enum para os tipos de alerta
    public enum TipoAlerta {
        FALHA,  // Equipamento inoperante
        ALERTA  // Risco de problema ou ineficiência
    }
}