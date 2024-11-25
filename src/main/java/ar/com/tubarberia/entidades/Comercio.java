package ar.com.tubarberia.entidades;

import ar.com.tubarberia.enumeraciones.Rol;
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
    private Long id;;

    private String nombre;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String telefono;

    private String direccion;

    @OneToOne
    private Imagen imagen;

    private Boolean activo = true;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(unique = true, nullable = false)
    private String CUIT;

    private String localidad;

    private String barrio;

    private LocalTime horarioApertura;

    private LocalTime horarioCierre;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> diasAbiertos;

    private String descripcion;

    private String rangoPrecios;

    @ElementCollection
    private List<String> especialidades;

    @OneToMany
    private List<Empleado> empleados;

    @OneToMany
    private List<Turno> turnos;

    private String sitioWeb;

    @ElementCollection
    private Map<String, String> redesSociales;

    private Double calificacionPromedio;

    private String politicasCancelacion;

}