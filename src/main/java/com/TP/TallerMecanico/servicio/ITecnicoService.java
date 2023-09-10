package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Tecnico;

import java.util.List;
public interface ITecnicoService {

    public List<Tecnico> listarTecnicos();
    public void guardar(Tecnico tecnico);
    public void eliminar(Tecnico tecnico);
    public Tecnico buscarTecnico(Tecnico tecnico);
}