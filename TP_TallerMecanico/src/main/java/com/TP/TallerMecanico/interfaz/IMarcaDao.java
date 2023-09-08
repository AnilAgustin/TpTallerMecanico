package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Marca;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IMarcaDao extends CrudRepository<Marca, Long>{
    
    @Modifying
    @Query("UPDATE Marca m SET m.estado = false WHERE m.idMarca = :idMarca")
    void marcarComoEliminado(@Param("idMarca") Long idMarca);
    
    List<Marca> findByEstadoTrue(); //arma una consulta SQL personalizada con estado

    @Modifying
    @Query("UPDATE Marca m SET m.estado = true WHERE m.idMarca = :idMarca")  //Update a estado true
    void marcarComoActivo(@Param("idMarca") Long idMarca);
    
    Marca findByNombre(String nombreMarca);
    
    Marca findByNombreAndEstadoTrue(String nombreMarca);
}
