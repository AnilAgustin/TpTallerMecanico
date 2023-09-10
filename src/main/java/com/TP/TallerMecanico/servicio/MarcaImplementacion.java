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
        Marca marcaActivada = marcaDao.findByNombreAndEstadoTrue(nombreMarca);

        if (!nombreMarca.trim().isEmpty()) {
            if (marcaExistente == null) {
                marcaDao.save(marca);
            } else {

                if (marcaActivada == null) {

                    marcaDao.marcarComoActivo(marcaExistente.getIdMarca());
                    for (Modelo modelo : marcaExistente.getModelos()) {
                        modeloService.activarModelo(modelo);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void actualizar(Marca marca){
        marca.setNombre(marca.getNombre().toUpperCase());
        Long marcaId = marca.getIdMarca();
        Marca marcaExistente = marcaDao.findById(marcaId).orElse(null);
        if(marcaExistente != null){
            String nuevoNombre = marca.getNombre();
            String nombreExistente = marcaExistente.getNombre();

            if(nuevoNombre.equals(nombreExistente) || !nombreExisteEnBaseDeDatos(nuevoNombre)){
                marcaDao.save(marca);
            }
        }
    }

    private boolean nombreExisteEnBaseDeDatos(String nombreMarca) {
        return marcaDao.findByNombre(nombreMarca) != null;
    }

    @Override
    @Transactional
    public void eliminar(Marca marca) {
        marcaDao.marcarComoEliminado(marca.getIdMarca());
        for (Modelo modelo : modeloDao.findByMarcaAndEstadoTrue(marca)) {
              modeloService.eliminar(modelo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Marca buscarMarca(Marca marca) {
        return marcaDao.findById(marca.getIdMarca()).orElse(null);
    }

}
