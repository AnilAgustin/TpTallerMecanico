package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Tecnico;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ITecnicoDao extends CrudRepository<Tecnico, Long> {

    @Modifying
    @Query("UPDATE Tecnico t SET t.estado = false WHERE t.idTecnico = :idTecnico")
    void marcarComoEliminado(@Param("idTecnico") Long idTecnico);

    List<Tecnico> findByEstadoTrue();

    @Modifying
    @Query("UPDATE Tecnico t SET t.estado = true WHERE t.idTecnico = :idTecnico")
    void marcarComoActivo(@Param("idTecnico") Long idTecnico);

    Tecnico findByLegajo(String legajo);

    Tecnico findByLegajoAndEstadoTrue(String legajo);
}