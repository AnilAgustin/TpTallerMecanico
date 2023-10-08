package com.TP.TallerMecanico.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

import lombok.Data;


@Data
@Entity
@Table(name="vehiculo")
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehiculo;

    @NotEmpty(message = "El campo no debe estar vacio")
    @Size(max=9, message = "La patente del vehiculo no debe superar los 9 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "La patente debe contener caracteres alfabéticos y numéricos")
    private String patente;

    @NotEmpty(message = "El año no debe estar vacio")
    @Pattern(regexp = "^(19|20)[0-9]{2}$", message = "Debe empezar con '19' o '20' seguido de dos dígitos.")
    @Pattern(regexp = "^[0-9]{1,4}$", message = "Debe ser un número de 1 a 4 dígitos.")
    private String year;

    @NotEmpty(message = "El kilometraje no debe estar vacio")
    @Pattern(regexp = "^[0-9]{1,8}$", message = "Debe ser un número de 1 a 8 dígitos.")
    private String kilometros;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;

    @OneToMany(mappedBy = "vehiculo")
    private List<Orden> ordenes;

    @Transient
    private String modo;

    private Boolean estado = true;

}
