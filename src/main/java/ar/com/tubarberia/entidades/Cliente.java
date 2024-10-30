package ar.com.tubarberia.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private String direccion;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    private String genero;

    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    private Boolean activo = true;

    @OneToMany(mappedBy = "cliente")
    private List<Turno> turnos;

    private String notas;

    private Integer puntosFidelidad = 0;

}

