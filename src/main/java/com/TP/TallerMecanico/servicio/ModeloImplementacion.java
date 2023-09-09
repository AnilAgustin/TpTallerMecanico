package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.interfaz.IModeloDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModeloImplementacion implements IModeloService {

    @Autowired
    private IModeloDao modeloDao;

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
        Modelo modeloRegistrada = modeloDao.findByNombreAndEstadoTrue(nombreModelo);

        if (!nombreModelo.trim().isEmpty()) {
            if (modeloExistente == null) {
                modeloDao.save(modelo);
            } else {
                if (modeloRegistrada == null) {
                    modeloDao.marcarComoActivo(modeloExistente.getIdModelo());
                }
            }
        }
    }

    @Override
    @Transactional
    public void eliminar(Modelo modelo) {
        modeloDao.marcarComoEliminado(modelo.getIdModelo());
    }

    @Override
    @Transactional(readOnly = true)
    public Modelo buscarModelo(Modelo modelo) {
        return modeloDao.findById(modelo.getIdModelo()).orElse(null);
    }

}
