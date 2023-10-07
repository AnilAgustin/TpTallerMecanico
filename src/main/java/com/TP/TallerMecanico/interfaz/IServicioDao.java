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

    @Modifying
    @Query("UPDATE Servicio m SET m.estado = true WHERE m.idServicio = :idServicio")  //Update a estado true
    void marcarComoActivo(@Param("idServicio") Long idServicio);

    Servicio findByNombre(String nombreServicio);
    Servicio findByNombreAndEstadoTrue(String nombreServicio);
    Servicio findByNombreAndEstadoFalse(String nuevoNombre);


}