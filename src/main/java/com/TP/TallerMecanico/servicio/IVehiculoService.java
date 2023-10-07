package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Vehiculo;

import java.util.List;
public interface IVehiculoService {

    public List<Vehiculo> listarVehiculos();
    public void guardar(Vehiculo vehiculo);
    public void actualizar(Vehiculo vehiculo);
    public void eliminar(Vehiculo vehiculo);
    public Vehiculo buscarVehiculo(Long idVehiculo);
    public void activarVehiculo(Vehiculo vehiculo) ;
}
