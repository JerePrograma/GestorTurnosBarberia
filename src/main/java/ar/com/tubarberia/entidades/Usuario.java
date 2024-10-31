package ar.com.tubarberia.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

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

}
