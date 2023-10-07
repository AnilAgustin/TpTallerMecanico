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

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @ManyToOne
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;

    @OneToMany(mappedBy = "vehiculo")
    private List<Orden> ordenes;

    @Transient
    private String modo;

    private Boolean estado = true;

}
