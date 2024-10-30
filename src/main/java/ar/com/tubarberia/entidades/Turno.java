package ar.com.tubarberia.entidades;

import ar.com.tubarberia.enumeraciones.EstadoOrden;
import ar.com.tubarberia.enumeraciones.EstadoTrabajo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Turno {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String titulo;

    @ManyToOne
    private Comercio comercio;

    @ManyToOne
    private Cliente cliente;

    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Temporal(TemporalType.DATE)
    private Date fechaActualizacion;

    @OneToOne
    private Imagen imagen;

    private Integer presupuesto;

    private String comentarioPresupuesto;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estadoOrden; // ABIERTO, CERRADO, CALIFICAR, CALIFICADO

    @Enumerated(EnumType.STRING)
    private EstadoTrabajo estadoTrabajo; // PRESUPUESTAR, PRESUPUESTADO, ACEPTADO, PRESUPUESTO_RECHAZADO,
    // TRABAJO_RECHAZADO, FINALIZADO, CANCELADO

    private Boolean estado;
}
