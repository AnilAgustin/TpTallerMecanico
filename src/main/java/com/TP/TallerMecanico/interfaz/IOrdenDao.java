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

    //FILTROS PARA MARCA
    //@Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND CAST(o.idOrden AS string) LIKE :numero%  AND o.fechaDOcumento =:fechaDocumento AND o.estado = true")
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.fechaDocumento BETWEEN :fechaDesdeDocumento AND :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYNumeroYFechaDesdeYFechaHasta(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("numero") Long numero, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.fechaDocumento >= :fechaDesdeDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYNumeroYFechaDesde(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("numero") Long numero, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento );
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.fechaDocumento <= :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYNumeroYFechaHasta(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("numero") Long numero, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento );

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYNumero(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("numero") Long numero);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModelo(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.fechaDocumento BETWEEN :fechaDesdeDocumento AND :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYFechaDesdeYFechaHasta(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.fechaDocumento >= :fechaDesdeDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYFechaDesde(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento);
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.vehiculo.modelo.marca.id = :marcaId AND o.fechaDocumento <= :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYModeloYFechaHasta(@Param("marcaId") Long marcaId, @Param("modeloId") Long modeloId, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYNumero(@Param("marcaId") Long marcaId, @Param("numero") Long numero);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.fechaDocumento BETWEEN :fechaDesdeDocumento AND :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYNumeroYFechaDesdeYFechaHasta(@Param("marcaId") Long marcaId, @Param("numero") Long numero, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento );
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.fechaDocumento >= :fechaDesdeDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYNumeroYFechaDesde(@Param("marcaId") Long marcaId, @Param("numero") Long numero, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento );
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.idOrden = :numero AND o.fechaDocumento <= :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYNumeroYFechaHasta(@Param("marcaId") Long marcaId, @Param("numero") Long numero, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento );

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.fechaDocumento BETWEEN :fechaDesdeDocumento AND :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYFechaDesdeYFechaHasta(@Param("marcaId") Long marcaId, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.fechaDocumento >= :fechaDesdeDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYFechaDesde(@Param("marcaId") Long marcaId, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento );
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.fechaDocumento <= :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorMarcaYFechaHasta(@Param("marcaId") Long marcaId, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.marca.id = :marcaId AND o.estado = true")
    List<Orden> filtrarOrdenPorMarca(@Param("marcaId") Long marcaId);


    //FILTROS PARA MODELOS
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.idOrden = :numero AND o.fechaDocumento BETWEEN :fechaDesdeDocumento AND :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYNumeroYFechaDesdeYFechaHasta(@Param("modeloId") Long modeloId, @Param("numero") Long numero, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.idOrden = :numero AND o.fechaDocumento >= :fechaDesdeDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYNumeroYFechaDesde(@Param("modeloId") Long modeloId, @Param("numero") Long numero, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento);
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.idOrden = :numero AND o.fechaDocumento <= :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYNumeroYFechaHasta(@Param("modeloId") Long modeloId, @Param("numero") Long numero, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.idOrden = :numero AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYNumero(@Param("modeloId") Long modeloId, @Param("numero") Long numero);

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.fechaDocumento BETWEEN :fechaDesdeDocumento AND :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYFechaDesdeYFechaHasta(@Param("modeloId") Long modeloId, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento );
    
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.fechaDocumento >= :fechaDesdeDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYFechaDesde(@Param("modeloId") Long modeloId, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento);
   
    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.fechaDocumento <= :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorModeloYFechaHasta(@Param("modeloId") Long modeloId, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento );

    @Query("SELECT o FROM Orden o WHERE o.vehiculo.modelo.id = :modeloId AND o.estado = true")
    List<Orden> filtrarOrdenPorModelo(@Param("modeloId") Long modeloId);


    //FILTRAR POR NUMERO
    @Query("SELECT o FROM Orden o WHERE o.idOrden = :numero AND o.fechaDocumento BETWEEN :fechaDesdeDocumento AND :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorNumeroYFechaDesdeYFechaHasta(@Param("numero") Long numero, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);
    
    @Query("SELECT o FROM Orden o WHERE o.idOrden = :numero AND o.fechaDocumento >= :fechaDesdeDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorNumeroYFechaDesde(@Param("numero") Long numero, @Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento);
    
    @Query("SELECT o FROM Orden o WHERE o.idOrden = :numero AND o.fechaDocumento <= :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorNumeroYFechaHasta(@Param("numero") Long numero, @Param("fechaHastaDocumento") LocalDate fechaHastaDocumento);

    @Query("SELECT o FROM Orden o WHERE o.idOrden = :numero AND o.estado = true")
    List<Orden> filtrarOrdenPorNumero(@Param("numero") Long numero);

    
    //FILTRAR POR FECHA
    @Query("SELECT o FROM Orden o WHERE o.fechaDocumento BETWEEN :fechaDesdeDocumento AND :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorFechaDesdeYFechaHasta(@Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento,@Param("fechaHastaDocumento") LocalDate fechaHastaDocumento );
   
    @Query("SELECT o FROM Orden o WHERE o.fechaDocumento >= :fechaDesdeDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorFechaDesde(@Param("fechaDesdeDocumento") LocalDate fechaDesdeDocumento);
    
    @Query("SELECT o FROM Orden o WHERE o.fechaDocumento <= :fechaHastaDocumento AND o.estado = true")
    List<Orden> filtrarOrdenPorFechaHasta(@Param("fechaHastaDocumento") LocalDate fechaHastaDocumento );

    //Estadisticas 
    @Query("SELECT MONTH(o.fechaDocumento) AS mes, SUM(CAST(d.subtotal AS DOUBLE)) AS recaudacion_total FROM Orden o JOIN o.detallesOrden d WHERE YEAR(o.fechaDocumento) = :year GROUP BY MONTH(o.fechaDocumento) ORDER BY MONTH(o.fechaDocumento)")    
    List<Object[]> obtenerIngresosMensuales(@Param("year") int year);

    List<Orden> findByFechaRegistro(LocalDate fechaOrden);
}