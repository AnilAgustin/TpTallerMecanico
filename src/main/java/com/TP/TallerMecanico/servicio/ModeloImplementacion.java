package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.interfaz.IModeloDao;
import com.TP.TallerMecanico.interfaz.IVehiculoDao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModeloImplementacion implements IModeloService {

    @Autowired
    private IModeloDao modeloDao;

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Autowired
    private IVehiculoService vehiculoService;

    private List<Vehiculo> vehiculosAntesDeEliminar;

    @Override
    @Transactional(readOnly = true)
    public List<Modelo> listarModelos() {
        return modeloDao.findByEstadoTrue();
    }

    @Override
    @Transactional
    public void guardar(Modelo modelo) {
        modelo.setNombre(modelo.getNombre().toUpperCase());
        String nombreModelo = modelo.getNombre();
        
        Marca marcaModelo = modelo.getMarca(); // Obtén la marca del modelo
        Modelo modeloExistente = modeloDao.findByNombreAndMarca(nombreModelo, marcaModelo);
        Modelo modeloActivado = modeloDao.findByNombreAndEstadoTrue(nombreModelo);

        if (!nombreModelo.trim().isEmpty()) {
            if (modeloExistente == null) {
                modeloDao.save(modelo);
            } else {
                if (modeloActivado == null) {
                    modeloDao.marcarComoActivo(modeloExistente.getIdModelo());
                    if (!modeloExistente.equals(modelo)) {
                        modelo.setIdModelo(modeloExistente.getIdModelo());
                        modeloDao.save(modelo);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void actualizar(Modelo modelo){
        Long modeloId = modelo.getIdModelo();
        Modelo modeloExistente = modeloDao.findById(modeloId).orElse(null);
        if (modeloExistente != null) {
            Marca marcaNueva = modelo.getMarca();
            Marca marcaExistente = modeloExistente.getMarca();

            if (marcaNueva.equals(marcaExistente) || !modeloExisteEnBaseDeDatos(marcaNueva)) {
                modelo.setNombre(modelo.getNombre().toUpperCase());
                modeloDao.save(modelo);
            }
        }
    }

    private boolean modeloExisteEnBaseDeDatos(Marca marca) {
        return modeloDao.findByMarca(marca) != null;
    }

    @Override
    @Transactional
    public void activarModelo(Modelo modelo){
        modeloDao.marcarComoActivo(modelo.getIdModelo());

        for (Vehiculo vehiculo : vehiculosAntesDeEliminar) {
            vehiculoService.activarVehiculo(vehiculo);
            System.out.println("ACTIVADO"+vehiculo.getPatente());
        }
        
    }

    @Override
    @Transactional
    public void eliminar(Modelo modelo) {
        modeloDao.marcarComoEliminado(modelo.getIdModelo());
        
        vehiculosAntesDeEliminar = vehiculoDao.findByModeloAndEstadoTrue(modelo);

        for (Vehiculo vehiculo: vehiculosAntesDeEliminar){
            vehiculoService.eliminar(vehiculo);
            System.out.println("ELIMINADO"+vehiculo.getPatente());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Modelo buscarModelo(Modelo modelo) {
        return modeloDao.findById(modelo.getIdModelo()).orElse(null);
    }

}
