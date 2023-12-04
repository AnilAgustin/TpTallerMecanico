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
    List<Vehiculo> findByEstadoFalse();

    @Modifying
    @Query("UPDATE Vehiculo m SET m.estado = true WHERE m.idVehiculo = :idVehiculo")
    void marcarComoActivo(@Param("idVehiculo") Long idVehiculo);

    @Modifying
    @Query("UPDATE Vehiculo m SET m.cliente.id = :clienteId WHERE m.idVehiculo = :vehiculoId")
    void actualizarIdCliente(@Param("vehiculoId") Long vehiculoId, @Param("clienteId") Long clienteId);

    Vehiculo findByPatente(String patente);

    Vehiculo findByPatenteAndEstadoTrue(String patente);

    List<Vehiculo> findByPatenteAndEstadoFalse(String patente);

    Vehiculo findByIdVehiculo(Long idVehiculo);

    Vehiculo findByIdVehiculoAndEstadoFalse(Long idVehiculo);

    List<Vehiculo> findByModeloAndEstadoTrue(Modelo modelo);

    List<Vehiculo> findByClienteAndEstadoTrue(Cliente cliente);

    //List<Vehiculo> findByTecnicoAndEstadoTrue(Tecnico tecnico);

    List<Vehiculo> findByPatenteAndModelo(String patente, Modelo modelo);

    @Query("SELECT v FROM Vehiculo v WHERE v.patente = :patente AND v.modelo.marca.id = :marcaId AND v.modelo.id = :modeloId AND v.estado = true")
    List<Vehiculo> filtrarVehiculo(@Param("patente") String patente, @Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId);


    @Query("SELECT v FROM Vehiculo v WHERE UPPER(v.patente) LIKE :patente% AND v.modelo.marca.id = :marcaId AND v.estado = true")
    List<Vehiculo> filtrarVehiculoPorPatenteYMarca(@Param("patente") String patente, @Param("marcaId") Long marcaId);

                               

    @Query("SELECT v FROM Vehiculo v WHERE UPPER(v.patente) LIKE :patente% AND v.modelo.id = :modeloId AND v.estado = true")
    List<Vehiculo> filtrarVehiculoPorPatenteYModelo(@Param("patente") String patente, @Param("modeloId") Long modeloId);

    
    @Query("SELECT v FROM Vehiculo v WHERE v.modelo.marca.id = :marcaId AND v.modelo.id = :modeloId AND v.estado = true")
    List<Vehiculo> filtrarVehiculoPorMarcaYModelo(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId);


    @Query("SELECT v FROM Vehiculo v WHERE UPPER(v.patente) LIKE :patente% AND v.estado = true")
    List<Vehiculo> filtrarVehiculoPorPatente(@Param("patente") String patente);
     

    @Query("SELECT v FROM Vehiculo v WHERE v.modelo.marca.id = :marcaId AND v.estado = true")
    List<Vehiculo> filtrarVehiculoPorMarca(@Param("marcaId") Long marcaId);
    

    @Query("SELECT v FROM Vehiculo v WHERE v.modelo.id = :modeloId AND v.estado=true")
    List<Vehiculo> filtrarVehiculoPorModelo(@Param("modeloId") Long modeloId);
    
    




}