package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Servicio;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IServicioDao extends CrudRepository<Servicio, Long>{
    
    @Modifying
    @Query("UPDATE Servicio m SET m.estado = false WHERE m.idServicio = :idServicio") //Query para el Soft Delete
    void marcarComoEliminado(@Param("idServicio") Long idServicio);
    
    List<Servicio> findByEstadoTrue(); //arma una consulta SQL personalizada con estado
    List<Servicio> findByEstadoFalse();

    Servicio findByIdServicio(Long idServicio);
    Servicio findByIdServicioAndEstadoFalse(Long idServicio);

    @Modifying
    @Query("UPDATE Servicio m SET m.estado = true WHERE m.idServicio = :idServicio")  //Update a estado true
    void marcarComoActivo(@Param("idServicio") Long idServicio);

    Servicio findByNombre(String nombreServicio);
    Servicio findByNombreAndEstadoTrue(String nombreServicio);
    Servicio findByNombreAndEstadoFalse(String nuevoNombre);

    @Query("SELECT s FROM Servicio s WHERE s.nombre LIKE :nombre% AND s.estado = true")
    List<Servicio> filtrarPorNombreYEstadoTrue(@Param("nombre") String nombre);

    @Query("SELECT s FROM Servicio s WHERE s.nombre LIKE :nombre% AND s.estado = false")
    List<Servicio> filtrarPorNombreYEstadoFalse(@Param("nombre") String nombre);

}