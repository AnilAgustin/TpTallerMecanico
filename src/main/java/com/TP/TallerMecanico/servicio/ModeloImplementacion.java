package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;
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


    @Override
    @Transactional(readOnly = true)
    public List<Modelo> listarModelos() {
        return modeloDao.findByEstadoTrue();
    }

    @Override
    @Transactional
    public void guardar(Modelo modelo) {
        modelo.setNombre(modelo.getNombre().toUpperCase());
        String nombreNuevo = modelo.getNombre();
        Marca marcaNueva = modelo.getMarca();

        Modelo modeloExistente = modeloDao.findByNombreAndMarcaAndEstadoTrue(nombreNuevo, marcaNueva);
        Modelo modeloActivado = modeloDao.findByNombreAndMarcaAndEstadoFalse(nombreNuevo, marcaNueva);

        //Verificacion para no guardar modelos repetidos 
        if (modeloActivado == null) {
            if (modeloExistente == null) {
                modeloDao.save(modelo);
            }
        } else {
            activarModelo(modeloActivado);
        }


    }

    @Override
    @Transactional
    public void actualizar(Modelo modelo){
        modelo.setNombre(modelo.getNombre().toUpperCase());
        Marca marcaNueva = modelo.getMarca();
        String nombreNuevo = modelo.getNombre();

        Modelo modeloViejo = modeloDao.findById(modelo.getIdModelo()).orElse(null);
        Marca marcaExistente = modeloViejo.getMarca();
        String nombreViejo = modeloViejo.getNombre();

        if (marcaNueva.equals((marcaExistente))){
            if (!nombreNuevo.equals((nombreViejo))) {
                Modelo checkNM1 = modeloDao.findByNombreAndMarca(nombreNuevo, marcaNueva);
                if (checkNM1 == null) {
                    modeloDao.save(modelo);
                }
            }
        } else {
            Modelo checkNM2 = modeloDao.findByNombreAndMarca(nombreNuevo, marcaNueva);
            if (checkNM2 == null){
                modeloDao.save(modelo);
            }


        }

    }


    @Override
    @Transactional
    public void activarModelo(Modelo modelo){
        modeloDao.marcarComoActivo(modelo.getIdModelo());
        for (Vehiculo vehiculo : modelo.getVehiculos()) {
            vehiculoService.activarVehiculo(vehiculo);
        }
        
    }

    @Override
    @Transactional
    public void eliminar(Modelo modelo) {
        modeloDao.marcarComoEliminado(modelo.getIdModelo());
        for (Vehiculo vehiculo: vehiculoDao.findByModeloAndEstadoTrue(modelo)){
            vehiculoService.eliminar(vehiculo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Modelo buscarModelo(Modelo modelo) {
        return modeloDao.findById(modelo.getIdModelo()).orElse(null);
    }

}
