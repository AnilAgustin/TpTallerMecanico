package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Imagen;
import com.TP.TallerMecanico.entidad.Orden;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
public interface IImagenDao extends CrudRepository<Imagen, Long> {

    List<Imagen> findByOrden(Orden orden);
}
