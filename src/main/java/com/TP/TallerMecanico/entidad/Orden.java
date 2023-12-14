package com.TP.TallerMecanico.entidad;

import java.time.LocalDate;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


@Data
@Entity
@Table(name="orden")
public class Orden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrden;

    private LocalDate fechaRegistro;

    private LocalDate fechaDocumento;

    @NotEmpty(message = "El kilometraje no debe estar vacio")
    @Pattern(regexp = "^[0-9]{1,8}$", message = "Debe ser un número de 1 a 8 dígitos.")
    private String kilometros;

    //Metodo para formatear los kilometros en miles
    public String formatearKilometros(){
        // Convertir kilómetros a tipo numérico
        double kilometrosNumerico = Double.parseDouble(kilometros);
    
        // Formateo del total
        NumberFormat formato = DecimalFormat.getNumberInstance(Locale.getDefault());
        ((DecimalFormat) formato).applyPattern("###,###.##");
    
        String kilometrosFormateados = formato.format(kilometrosNumerico);
        return kilometrosFormateados+" Km";
    }

    @OneToMany(mappedBy = "orden")
    private List<DetalleOrden> detallesOrden;

    @OneToMany(mappedBy = "orden")
    private List<Imagen> imagenesOrden;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estadoActual;

    @Transient
    private String modo;
    private Boolean estado = true;

    public List<DetalleOrden> getDetallesOrden() {
        return detallesOrden;
    }

    public List<DetalleOrden> getDetallesOrdenesHTML(){
        if (detallesOrden.isEmpty()){
            return null;
        } else {
            return detallesOrden;
        }
    }

    
    public String calcularTotal() {
        int total = 0;
        for (DetalleOrden detalleOrden : detallesOrden) {
            if (detalleOrden.getEstado()) {
                total += detalleOrden.getSubtotal();
            }
        }

        //Formateo del total
        NumberFormat formato = DecimalFormat.getNumberInstance(Locale.getDefault());
        ((DecimalFormat) formato).applyPattern("###,###.##");

        String totalFormateado = formato.format(total);

        return "$"+totalFormateado;
    }
}