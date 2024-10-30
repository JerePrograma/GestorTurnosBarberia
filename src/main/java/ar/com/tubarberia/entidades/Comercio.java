package ar.com.tubarberia.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String direccion;

    private String telefono;

    private String email;

    private String cuit;

    private LocalTime horarioApertura;

    private LocalTime horarioCierre;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> diasAbiertos;

    private String descripcion;

    private String rangoPrecios;

    @ElementCollection
    private List<String> especialidades;

    @OneToMany(mappedBy = "comercio")
    private List<Empleado> empleados;

    @OneToMany(mappedBy = "comercio")
    private List<Turno> turnos;

    private Boolean activo = true;

    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    private String ubicacion;

    private String sitioWeb;

    @ElementCollection
    private Map<String, String> redesSociales;

    private Double calificacionPromedio;

    private String politicasCancelacion;
}