package com.TP.TallerMecanico.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
@Entity
@Table(name="servicio")
public class Servicio implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;
    
    @NotEmpty(message = "El campo no debe estar vacio")
    @Size(max=50, message = "El nombre de la marca no debe superar los 50 caracteres")
    @Pattern(regexp = "^(?!\s*$)[a-zA-Z\s]+$",message = "El nombre de la marca debe contener solo caracteres alfabeticos")    
    private String nombre;
    

    private int precio;
    private String repuesto;

    @Transient
    private String modo;
    private Boolean estado = true;

    @OneToMany(mappedBy = "servicio")
    private List<DetalleOrden> detallesOrden;


}