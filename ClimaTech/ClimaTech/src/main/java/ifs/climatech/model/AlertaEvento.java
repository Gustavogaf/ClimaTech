package ifs.climatech.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "alerta_eventos")
public class AlertaEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alerta_id", nullable = false)
    private Alerta alerta; // O tipo de alerta que foi disparado (ex: "CONSUMO_ALTO")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento; // O equipamento que gerou o alerta

    @Column(nullable = false)
    private LocalDateTime timestamp; // Quando o alerta ocorreu

    private String mensagem; // Uma mensagem descritiva (ex: "Consumo atingiu 8.5 kWh")

    private boolean resolvido = false; // Status para saber se a manutenção já resolveu
}