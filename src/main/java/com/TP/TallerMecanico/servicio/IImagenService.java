package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Imagen;
import com.TP.TallerMecanico.entidad.Orden;

import java.util.List;
public interface IImagenService {

    public List<Imagen> listarImagenes(Orden orden);

    public void guardar(Imagen imagen);

    public void eliminar(Imagen imagen);
}
