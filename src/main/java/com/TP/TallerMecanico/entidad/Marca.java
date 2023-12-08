package com.TP.TallerMecanico.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
@Entity
@Table(name="marca")
public class Marca implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMarca;
    
    @NotEmpty(message = "El campo no debe estar vacio")
    @Size(max=50, message = "El nombre de la marca no debe superar los 50 caracteres")
    @Size(max=50, message = "El nombre de la marca no debe superar los 50 caracteres")
    @Pattern(regexp = "^(?!\s*$)[a-zA-Z\s]+$",message = "El nombre de la marca debe contener solo caracteres alfabeticos\n")
    private String nombre;

    @NotNull(message = "El campo no debe estar vacio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El impuesto debe ser mayor que cero")
    @DecimalMax(value = "100.0", inclusive = true, message = "El impuesto no puede ser mayor que 100%")
    private BigDecimal impuesto;
    
    @NotNull(message = "El campo no debe estar vacio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El impuesto debe ser mayor que cero")
    @DecimalMax(value = "100.0", inclusive = true, message = "El impuesto no puede ser mayor que 100%")
    private Double impuesto;

    @Transient
    private String modo;
    private Boolean estado = true;

    @OneToMany(mappedBy = "marca")
    private List<Modelo> modelos;


    public List<Modelo> getModelos(){
        return modelos;
    }
}