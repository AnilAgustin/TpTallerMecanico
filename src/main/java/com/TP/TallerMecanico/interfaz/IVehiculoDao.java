package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.entidad.Vehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IVehiculoDao extends CrudRepository<Vehiculo, Long> {

    @Modifying
    @Query("UPDATE Vehiculo m SET m.estado = false WHERE m.idVehiculo = :idVehiculo")
    void marcarComoEliminado(@Param("idVehiculo") Long idVehiculo);

    List<Vehiculo> findByEstadoTrue();

    @Modifying
    @Query("UPDATE Vehiculo m SET m.estado = true WHERE m.idVehiculo = :idVehiculo")
    void marcarComoActivo(@Param("idVehiculo") Long idVehiculo);

    Vehiculo findByPatente(String patente);

    Vehiculo findByPatenteAndEstadoTrue(String patente);

    Vehiculo findByIdVehiculo(Long idVehiculo);

    List<Vehiculo> findByModeloAndEstadoTrue(Modelo modelo);
}