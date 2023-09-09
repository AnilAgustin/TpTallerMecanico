package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ITecnicoDao extends CrudRepository<Cliente, Long> {

    @Modifying
    @Query("UPDATE Tecnico t SET t.estado = false WHERE t.idTecnico = :idTecnico")
    void marcarComoEliminado(@Param("idTecnico") Long idTecnico);

    List<Cliente> findByEstadoTrue();

    @Modifying
    @Query("UPDATE Tecnico t SET t.estado = true WHERE m.idTecnico = :idTecnico")
    void marcarComoActivo(@Param("idTecnico") Long idTecnico);

    Cliente findByDni(String dni);

    Cliente findByDniAndEstadoTrue(String dni);
}