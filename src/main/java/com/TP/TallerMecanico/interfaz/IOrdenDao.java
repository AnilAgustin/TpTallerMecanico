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
    List<Orden> findByTecnicoAndEstadoTrue(Tecnico tecnico);
    
    //QUERYS PARA BUSQUEDA

    //@Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND CAST(o.idOrden AS string) LIKE :numero%  AND o.fechaDOcumento =:fechaDocumento AND o.estado = true")
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.fechaDocumento = :fechaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYNumeroYFechaDocumento(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("numero") Long numero, @Param("fechaDocumento") LocalDate fechaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYNumero(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("numero") Long numero);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModelo(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.fechaDocumento = :fechaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYFechaDocumento(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("fechaDocumento") LocalDate fechaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYNumero(@Param("marcaId") Long marcaId, @Param("numero") Long numero);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.fechaDocumento = :fechaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYNumeroYFechaDocumento(@Param("marcaId") Long marcaId, @Param("numero") Long numero, @Param("fechaDocumento") LocalDate fechaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.idOrden = :numero AND o.fechaDocumento = :fechaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYNumeroYFechaDocumento(@Param("modeloId") Long modeloId, @Param("numero") Long numero, @Param("fechaDocumento") LocalDate fechaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.idOrden = :numero AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYNumero(@Param("modeloId") Long modeloId, @Param("numero") Long numero);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.estado = true")
    List<Orden> filtrarOrdenPorMarca(@Param("marcaId") Long marcaId);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.estado = true")
    List<Orden> filtrarOrdenPorModelo(@Param("modeloId") Long modeloId);

    @Query("SELECT o FROM Orden o WHERE o.idOrden = :numero AND o.estado = true")
    List<Orden> filtrarOrdenPorNumero(@Param("numero") Long numero);

    @Query("SELECT o FROM Orden o WHERE o.fechaDocumento = :fechaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorFecha(@Param("fechaDocumento") LocalDate fechaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.fechaDocumento = :fechaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYFechaDocumento(@Param("marcaId") Long marcaId, @Param("fechaDocumento") LocalDate fechaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.fechaDocumento = :fechaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYFechaDocumento(@Param("modeloId") Long modeloId, @Param("fechaDocumento") LocalDate fechaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.idOrden = :numero AND o.fechaDocumento = :fechaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorNumeroYFechaDocumento(@Param("numero") Long numero, @Param("fechaDocumento") LocalDate fechaDocumento);

    List<Orden> findByFechaRegistro(LocalDate fechaOrden);
}