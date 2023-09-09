package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.interfaz.IMarcaDao;
import com.TP.TallerMecanico.interfaz.IModeloDao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarcaImplementacion implements IMarcaService {

    @Autowired
    private IMarcaDao marcaDao;

    @Autowired
    private IModeloDao modeloDao;

    @Autowired 
    private IModeloService modeloService;

    private List<Modelo> modelosAntesDeEliminar; 

    @Override
    @Transactional(readOnly = true)
    public List<Marca> listarMarcas() {
        return marcaDao.findByEstadoTrue();
    }

    @Override
    @Transactional
    public void guardar(Marca marca) {
        marca.setNombre(marca.getNombre().toUpperCase());
        String nombreMarca = marca.getNombre();
        Marca marcaExistente = marcaDao.findByNombre(nombreMarca);
        Marca marcaRegistrada = marcaDao.findByNombreAndEstadoTrue(nombreMarca);

        if (!nombreMarca.trim().isEmpty()) {
            if (marcaExistente == null) {
                marcaDao.save(marca);
                
            } else {

                if (marcaRegistrada == null) {

                    marcaDao.marcarComoActivo(marcaExistente.getIdMarca());

                    if (modelosAntesDeEliminar != null) {
                        for (Modelo modelo : modelosAntesDeEliminar) {
                            modeloService.activarModelo(modelo);
                        }
                    }
                }
            }
        }
    }


    @Override
    @Transactional
    public void eliminar(Marca marca) {
        marcaDao.marcarComoEliminado(marca.getIdMarca());

        modelosAntesDeEliminar =  modeloDao.findByMarcaAndEstadoTrue(marca);

        for (Modelo modelo : modelosAntesDeEliminar) {
            modeloService.eliminar(modelo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Marca buscarMarca(Marca marca) {
        return marcaDao.findById(marca.getIdMarca()).orElse(null);
    }

}
