package com.TP.TallerMecanico.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name="modelo")
public class Modelo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idModelo;
    
    @NotEmpty(message = "El campo no debe estar vacio")
    @Size(max=50, message = "El nombre de la marca no debe superar los 50 caracteres") 
    @Pattern(regexp = "^(?!\s*$)[a-zA-Z\\d\\s]+$", message = "El nombre de la marca debe contener caracteres alfabéticos y números")
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    private Boolean estado = true;
}
