package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Imagen;
import com.TP.TallerMecanico.entidad.Orden;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface IImagenDao extends CrudRepository<Imagen, Long> {

    List<Imagen> findByOrden(Orden orden);
}
