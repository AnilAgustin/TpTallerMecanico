package com.TP.TallerMecanico.entidad;

import lombok.Data;

@Data
public class Modelo {
    
    private Long idModelo;
    private String nombre;
    private Marca modelo;
    private Boolean estado;
}
