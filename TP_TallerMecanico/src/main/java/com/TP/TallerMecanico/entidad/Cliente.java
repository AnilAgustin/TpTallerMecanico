package com.TP.TallerMecanico.entidad;

import lombok.Data;

@Data
public class Cliente {
    
    private Integer razonSocial;
    private String nombre;
    private String apellido;
    private Vehiculo vehiculoCliente;
}
