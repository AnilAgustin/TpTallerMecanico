package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Imagen;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.interfaz.IImagenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImagenImplementacion implements IImagenService {

    @Autowired
    private IImagenDao imagenDao;

    @Override
    @Transactional(readOnly = true)
    public List<Imagen> listarImagenes(Orden orden){
        return imagenDao.findByOrden(orden);
    }

    @Override
    @Transactional
    public void guardar(Imagen imagen){
        imagenDao.save(imagen);
    }

    @Override
    @Transactional
    public void eliminar(Imagen imagen){
        imagenDao.deleteById(imagen.getIdImagen());
    }
}
