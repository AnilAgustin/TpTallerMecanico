package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IClienteDao extends CrudRepository<Cliente, Long> {

    @Modifying
    @Query("UPDATE Cliente m SET m.estado = false WHERE m.idCliente = :idCliente")
    void marcarComoEliminado(@Param("idCliente") Long idCliente);

    List<Cliente> findByEstadoTrue();

    @Modifying
    @Query("UPDATE Cliente m SET m.estado = true WHERE m.idCliente = :idCliente")
    void marcarComoActivo(@Param("idCliente") Long idCliente);

    Cliente findByDni(String dni);

    Cliente findByIdCliente(Long idCliente);

    Cliente findByDniAndEstadoTrue(String dni);


}