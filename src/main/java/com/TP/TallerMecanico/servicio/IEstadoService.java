package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Estado;
import java.util.List;
public interface IEstadoService {

    public List<Estado> listarEstados();
    public void guardar(Estado estado);

}
