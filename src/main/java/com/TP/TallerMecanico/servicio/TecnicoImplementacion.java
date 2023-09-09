package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.interfaz.ITecnicoDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TecnicoImplementacion implements ITecnicoService {

    @Autowired
    private ITecnicoDao tecnicoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Tecnico> listarTecnicos() { return tecnicoDao.findByEstadoTrue(); }

    @Override
    @Transactional
    public void guardar(Tecnico tecnico) {
        String legajo = tecnico.getLegajo();
        Tecnico tecnicoExistente = tecnicoDao.findByLegajo(legajo);
        Tecnico tecnicoRegistrado = tecnicoDao.findByLegajoAndEstadoTrue(legajo);

        if (tecnicoExistente == null) {
            tecnicoDao.save(tecnico);
        } else {
            if (tecnicoRegistrado == null) {
                tecnicoDao.marcarComoActivo(tecnicoExistente.getIdTecnico());
                }
            }
    }

    @Override
    @Transactional
    public void eliminar(Tecnico tecnico) {
        tecnicoDao.marcarComoEliminado(tecnico.getIdTecnico());
    }

    @Override
    @Transactional(readOnly = true)
    public Tecnico buscarTecnico(Tecnico tecnico) {
        return tecnicoDao.findById(tecnico.getIdTecnico()).orElse(null);
    }
}