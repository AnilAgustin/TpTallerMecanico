package com.TP.TallerMecanico.interfaz;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.entidad.Tecnico;


import java.time.LocalDate;
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
    List<Orden> findByTecnico(Tecnico tecnico);
    
    //QUERYS PARA BUSQUEDA

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId")
    List<Orden> filtrarVehiculoPorMarcaYModelo(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId);


    @Query("SELECT o FROM Orden o WHERE  o.vehiculo.modelo.marca.id = :marcaId")
    List<Orden> filtrarVehiculoPorMarca(@Param("marcaId") Long marcaId);

    @Query("SELECT o FROM Orden o WHERE  o.vehiculo.modelo.id = :modeloId")
    List<Orden> filtrarVehiculoPorModelo(@Param("modeloId") Long modeloId);

    List<Orden> findByFecha(LocalDate fechaOrden);
    


}