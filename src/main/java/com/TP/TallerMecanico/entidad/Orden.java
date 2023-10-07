package com.TP.TallerMecanico.entidad;

import jakarta.persistence.*;
// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.Pattern;
// import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

//import org.springframework.jmx.export.annotation.ManagedResource;

import lombok.Data;

@Data
@Entity
@Table(name="orden")
public class Orden implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrden;
    
    

    @OneToMany(mappedBy = "orden")
    private List<DetalleOrden> detallesOrden;

    @ManyToOne
    @JoinColumn(name="vehiculo_id")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name="tecnico_id")
    private Tecnico tecnico;

    @Transient
    private String modo;
    private Boolean estado = true;

    public List<DetalleOrden> getDetallesOrden(){
        return detallesOrden;
    }
}