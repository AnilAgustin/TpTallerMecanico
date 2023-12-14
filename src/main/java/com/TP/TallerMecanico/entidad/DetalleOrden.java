package com.TP.TallerMecanico.entidad;
import jakarta.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import jakarta.validation.constraints.Min;
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

    //Formateo del subtotal a miles
    public String subtotalFormateado(){
        
        NumberFormat formato = DecimalFormat.getNumberInstance(Locale.getDefault());
        ((DecimalFormat) formato).applyPattern("###,###.##");
    
        String subtotalFormateado = formato.format(subtotal);
        return "$"+subtotalFormateado;
    }

    @Min(value = 0, message = "El precio final del servicio debe ser mayor o igual a cero")
    private int precioFinalServicio;

    //Formateo del precioFinalServicio a miles
    public String precioFinalServicioFormateado(){
    
        NumberFormat formato = DecimalFormat.getNumberInstance(Locale.getDefault());
        ((DecimalFormat) formato).applyPattern("###,###.##");
    
        String precioFinalServicioFormateado = formato.format(precioFinalServicio);
        return "$"+precioFinalServicioFormateado;
    
    }
    
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