package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Vehiculo;

import java.util.List;
public interface IVehiculoService {

    public List<Vehiculo> listarVehiculos();
    public void guardar(Vehiculo vehiculo);
    public void eliminar(Vehiculo vehiculo);
    public Vehiculo buscarVehiculo(Vehiculo vehiculo);
    public void activarVehiculo(List<Vehiculo> vehiculos) ;
}
