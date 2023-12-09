package com.TP.TallerMecanico.interfaz;
import com.TP.TallerMecanico.entidad.Estado;


import org.springframework.data.repository.CrudRepository;

public interface IEstadoDao extends CrudRepository<Estado, Long>{
    Estado findByNombre(String nombre);
    Estado findByIdEstado(Long id);
}
