package com.TP.TallerMecanico.entidad;

import jakarta.persistence.*;
// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.Pattern;
// import jakarta.validation.constraints.Size;

import java.io.Serializable;

import lombok.Data;

@Data
@Entity
@Table(name="detalleOrden")
public class DetalleOrden implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleOrden;
    
    private int subtotal;
    
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden orden;



    @Transient
    private String modo;
    private Boolean estado = true;

}