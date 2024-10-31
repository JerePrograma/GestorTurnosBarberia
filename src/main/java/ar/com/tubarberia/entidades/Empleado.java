package ar.com.tubarberia.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado extends Usuario {

    private String puesto;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

    private Date fechaContratacion;
/*
    private Long salario;*/
}

