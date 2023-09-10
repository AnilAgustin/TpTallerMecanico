package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.interfaz.ITecnicoDao;
import java.util.List;

import com.TP.TallerMecanico.interfaz.IVehiculoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TecnicoImplementacion implements ITecnicoService {

    @Autowired
    private ITecnicoDao tecnicoDao;

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Autowired
    private IVehiculoService vehiculoService;
    @Override
    @Transactional(readOnly = true)
    public List<Tecnico> listarTecnicos() { return tecnicoDao.findByEstadoTrue(); }

    @Override
    @Transactional
    public void guardar(Tecnico tecnico) {
        String legajo = tecnico.getLegajo();
        Tecnico legajoExistente = tecnicoDao.findByLegajo(legajo);
        Tecnico legajoActivado = tecnicoDao.findByLegajoAndEstadoTrue(legajo);

        if (legajoExistente == null) {
            tecnicoDao.save(tecnico);
        } else {
            if (legajoActivado == null){
                tecnicoDao.marcarComoActivo(legajoExistente.getIdTecnico());
                for (Vehiculo vehiculo : legajoExistente.getVehiculos()){
                    vehiculoService.activarVehiculo(vehiculo);
                }
                if (!legajoExistente.equals(tecnico)){
                    tecnico.setIdTecnico(legajoExistente.getIdTecnico());
                    tecnicoDao.save(tecnico);
                }
            }
        }
    }

    @Override
    @Transactional
    public void actualizar(Tecnico tecnico){
        Long tecnicoId = tecnico.getIdTecnico();
        Tecnico tecnicoExistente = tecnicoDao.findById(tecnicoId).orElse(null);
        if (tecnicoExistente != null) {
            // Verificar si el tecnico ha cambiado
            String nuevoLegajo = tecnico.getLegajo();
            String legajoExistente = tecnicoExistente.getLegajo();

            if (nuevoLegajo.equals(legajoExistente) || !legajoExisteEnBaseDeDatos(nuevoLegajo)) {
                tecnicoDao.save(tecnico);
            }
        }
    }   

    private boolean legajoExisteEnBaseDeDatos(String legajo) {
        return tecnicoDao.findByLegajo(legajo) != null;
    }

    @Override
    @Transactional
    public void eliminar(Tecnico tecnico) {
        tecnicoDao.marcarComoEliminado(tecnico.getIdTecnico());
        for (Vehiculo vehiculo: vehiculoDao.findByTecnicoAndEstadoTrue(tecnico)){
            vehiculoService.eliminar(vehiculo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Tecnico buscarTecnico(Long idTecnico) {
        return tecnicoDao.findById(idTecnico).orElse(null);
    }
}