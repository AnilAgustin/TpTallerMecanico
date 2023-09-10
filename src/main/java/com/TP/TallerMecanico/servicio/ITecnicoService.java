package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.entidad.Vehiculo;

import java.util.List;
public interface ITecnicoService {

    public List<Tecnico> listarTecnicos();
    public void guardar(Tecnico tecnico);
    public void actualizar(Tecnico tecnico);
    public void eliminar(Tecnico tecnico);
    public Tecnico buscarTecnico(Long idTecnico);
}
