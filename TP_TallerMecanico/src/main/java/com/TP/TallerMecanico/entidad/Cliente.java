package com.TP.TallerMecanico.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.Data;


@Data
@Entity
@Table(name="cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    //@NotEmpty(message = "El campo no debe estar vacio")
    //@Size(max=50, message = "El nombre del cliente no debe superar los 50 caracteres")
    //@Pattern(regexp = "^(?!\s*$)[a-zA-Z\s]+$",message = "El nombre del debe contener solo caracteres alfabeticos")

    private Integer razonSocial;
    private String nombre;
    private String apellido;
//    private Vehiculo vehiculoCliente;

    private Boolean estado = true;

    public Long getidCliente() {
        return idCliente;
    }

    public Integer getrazonSocial() {
        return razonSocial;
    }

    public String getnombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Boolean getEstado() {
        return estado;
    }


}