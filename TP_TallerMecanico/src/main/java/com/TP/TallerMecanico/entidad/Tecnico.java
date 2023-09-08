package com.TP.TallerMecanico.entidad;

import lombok.Data;

@Data
public class Tecnico {
    
    private Long legajo;
    private String nombre;
    private String apellido;
    private Vehiculo vehiculoTecnico;
}
