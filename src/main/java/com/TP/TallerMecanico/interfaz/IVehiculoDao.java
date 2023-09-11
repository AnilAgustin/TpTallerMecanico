package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.entidad.Vehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IVehiculoDao extends CrudRepository<Vehiculo, Long> {

    @Modifying
    @Query("UPDATE Vehiculo m SET m.estado = false WHERE m.idVehiculo = :idVehiculo") //Query para el Soft Delete
    void marcarComoEliminado(@Param("idVehiculo") Long idVehiculo);

    List<Vehiculo> findByEstadoTrue();

    @Modifying
    @Query("UPDATE Vehiculo m SET m.estado = true WHERE m.idVehiculo = :idVehiculo")
    void marcarComoActivo(@Param("idVehiculo") Long idVehiculo);

    @Modifying
    @Query("UPDATE Vehiculo m SET m.cliente.id = :clienteId WHERE m.idVehiculo = :vehiculoId")
    void actualizarIdCliente(@Param("vehiculoId") Long vehiculoId, @Param("clienteId") Long clienteId);

    Vehiculo findByPatente(String patente);

    Vehiculo findByPatenteAndEstadoTrue(String patente);

    Vehiculo findByIdVehiculo(Long idVehiculo);

    List<Vehiculo> findByModeloAndEstadoTrue(Modelo modelo);

    List<Vehiculo> findByClienteAndEstadoTrue(Cliente cliente);

    List<Vehiculo> findByTecnicoAndEstadoTrue(Tecnico tecnico);
}