package com.TP.TallerMecanico.entidad;

import jakarta.persistence.*;
// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.Pattern;
// import jakarta.validation.constraints.Size;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name="detalleOrden")
public class DetalleOrden implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleOrden;

    @NotEmpty(message = "El campo no debe estar vacio")
    @Pattern(regexp = "^[0-9]+$", message = "La cantidad debe contener solo números")
    private String cantidad;

    private int subtotal;

    private int precioFinalServicio;
    
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden orden;

    public int calcularSubtotal(){
        int cantidad = Integer.parseInt(this.cantidad);
        return (cantidad * precioFinalServicio);
    }


    @Transient
    private String modo;
    private Boolean estado = true;

}