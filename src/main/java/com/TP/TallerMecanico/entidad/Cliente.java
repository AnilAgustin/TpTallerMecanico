package com.TP.TallerMecanico.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

import lombok.Data;


@Data
@Entity
@Table(name="cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

   
    @NotEmpty(message = "El campo no debe estar vacio")
    @Pattern(regexp = "^[0-9]{1,8}$", message = "El DNI debe contener solo números y tener un máximo de 8 dígitos")
    private String dni;
    
    @NotEmpty(message = "El campo no debe estar vacio")
    @Size(max=50, message = "El nombre del cliente no debe superar los 50 caracteres")
    @Pattern(regexp = "^(?!\s*$)[a-zA-Z\s]+$",message = "El nombre debe contener solo caracteres alfabeticos")
    private String nombre;

    @NotEmpty(message = "El campo no debe estar vacio")
    @Size(max=50, message = "El nombre del cliente no debe superar los 50 caracteres")
    @Pattern(regexp = "^(?!\s*$)[a-zA-Z\s]+$",message = "El apellido debe contener solo caracteres alfabeticos")
    private String apellido;

    @OneToMany(mappedBy = "cliente")
    private List<Vehiculo> vehiculo;

    @Transient
    private String modo;
    private Boolean estado = true;

}