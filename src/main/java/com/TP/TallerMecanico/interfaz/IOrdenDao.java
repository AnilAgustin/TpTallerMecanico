package com.TP.TallerMecanico.interfaz;
import com.TP.TallerMecanico.entidad.Orden;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IOrdenDao extends CrudRepository<Orden, Long> {


    @Modifying
    @Query("UPDATE Orden m SET m.estado = false WHERE m.idOrden = :idOrden") //Query para el Soft Delete
    void marcarComoEliminado(@Param("idOrden") Long idOrden);

    List<Orden> findByEstadoTrue();

    @Modifying
    @Query("UPDATE Orden m SET m.estado = true WHERE m.idOrden = :idOrden")
    void marcarComoActivo(@Param("idOrden") Long idOrden);

    Orden findByIdOrden(Long idOrden);
    Orden findByIdOrdenAndEstadoTrue(Long idOrden);


}