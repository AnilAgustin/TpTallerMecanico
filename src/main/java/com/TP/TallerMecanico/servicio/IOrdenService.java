package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Orden;

import java.util.List;
public interface IOrdenService {

    public List<Orden> listarOrdenes();
    public void guardar(Orden orden);
    public void actualizar(Orden orden);
    public void eliminar(Orden orden);
    public Orden buscarOrden(Long idOrden);
    public void activarOrden(Orden orden) ;
}