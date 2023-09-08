package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.interfaz.IMarcaDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarcaImplementacion implements MarcaService{

    @Autowired
    private IMarcaDao marcaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Marca> listarMarcas() {
        return marcaDao.findByEstadoTrue();  
    }

    @Override
    @Transactional
    public void guardar(Marca marca) {
        String nombreMarca = marca.getNombre();
        Marca marcaExistente = marcaDao.findByNombreAndEstadoFalse(nombreMarca);
        if(marcaExistente != null){
            marcaDao.marcarComoActivo(marcaExistente.getIdMarca());
        }else{
            marcaDao.save(marca);
        }
    }

    @Override
    @Transactional
    public void eliminar(Marca marca) {
        marcaDao.marcarComoEliminado(marca.getIdMarca());
    }

    @Override
    @Transactional(readOnly = true)
    public Marca buscarMarca(Marca marca) {
        return marcaDao.findById(marca.getIdMarca()).orElse(null);
    }
    
}
