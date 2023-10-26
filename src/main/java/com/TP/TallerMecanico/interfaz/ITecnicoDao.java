package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Tecnico;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ITecnicoDao extends CrudRepository<Tecnico, Long> {

    @Modifying
    @Query("UPDATE Tecnico t SET t.estado = false WHERE t.idTecnico = :idTecnico") //Query para el Soft Delete
    void marcarComoEliminado(@Param("idTecnico") Long idTecnico);

    List<Tecnico> findByEstadoTrue();

    @Modifying
    @Query("UPDATE Tecnico t SET t.estado = true WHERE t.idTecnico = :idTecnico")
    void marcarComoActivo(@Param("idTecnico") Long idTecnico);

    Tecnico findByLegajo(String legajo);

    Tecnico findByLegajoAndEstadoTrue(String legajo);

    @Query("SELECT t FROM Tecnico t WHERE t.nombre LIKE :nombre% AND t.apellido LIKE :apellido% AND t.legajo = :legajo AND t.estado = true")
    List<Tecnico> filtrarTecnicoPorNombreYApellidoYLegajo(@Param("nombre") String nombre, @Param("apellido") String apellido, @Param("legajo") String legajo);

    @Query("SELECT t FROM Tecnico t WHERE t.nombre LIKE :nombre% AND t.apellido LIKE :apellido% AND t.estado = true")
    List<Tecnico> filtrarTecnicoPorNombreYApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

    @Query("SELECT t FROM Tecnico t WHERE t.nombre LIKE :nombre%  AND t.legajo = :legajo AND t.estado = true")
    List<Tecnico> filtrarTecnicoPorNombreYLegajo(@Param("nombre") String nombre, @Param("legajo") String legajo);
    
    @Query("SELECT t FROM Tecnico t WHERE t.apellido LIKE :apellido%  AND t.legajo = :legajo AND t.estado = true")
    List<Tecnico> filtrarTecnicoPorApellidoYLegajo(@Param("apellido") String apellido, @Param("legajo") String legajo);

    @Query("SELECT t FROM Tecnico t WHERE t.nombre LIKE :nombre%  AND t.estado = true")
    List<Tecnico> filtrarTecnicoPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT t FROM Tecnico t WHERE t.apellido LIKE :apellido%  AND t.estado = true")
    List<Tecnico> filtrarTecnicoPorApellido(@Param("apellido") String apellido);

    @Query("SELECT t FROM Tecnico t WHERE t.legajo = :legajo AND t.estado = true")
    List<Tecnico> filtrarTecnicoPorLegajo(@Param("legajo") String legajo);
}