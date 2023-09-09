package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Modelo;
import java.util.List;

public interface IModeloService {

    public List<Modelo> listarModelos();
    public void guardar(Modelo modelo);
    public void eliminar(Modelo modelo);
    public Modelo buscarModelo(Modelo modelo);
}
