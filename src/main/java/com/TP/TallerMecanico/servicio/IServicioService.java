package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Servicio;
import java.util.List;

public interface IServicioService {

    public List<Servicio> listarServicios();
    public void guardar(Servicio servicio);
    public void actualizar(Servicio servicio);
    public void eliminar(Servicio servicio);
    public Servicio buscarServicio(Servicio servicio);
    public void activarServicio(Servicio servicio);

}