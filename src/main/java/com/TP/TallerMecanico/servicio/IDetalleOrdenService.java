package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Orden;

import java.util.List;

public interface IDetalleOrdenService {

    public List<DetalleOrden> listarDetallesOrden();
    public void guardar(DetalleOrden detalleOrden);
    public void actualizar(DetalleOrden detalleOrden, Long idOrden);
    public void eliminar(DetalleOrden detalleOrden);
    public DetalleOrden buscarDetalleOrden(Long idDetalleOrden);
    public void activarDetalleOrden(DetalleOrden detalleOrden);

    public List<DetalleOrden> listarDetallesPorOrden(Orden orden);
    public List<DetalleOrden> findByIdOrden(Long idOrden);

   
}