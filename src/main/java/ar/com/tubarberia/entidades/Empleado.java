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
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private String puesto;

    @Temporal(TemporalType.DATE)
    private Date fechaContratacion;

    private Long salario;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

    @OneToMany(mappedBy = "empleado")
    private List<Turno> turnos;

    private Boolean activo = true;

}

