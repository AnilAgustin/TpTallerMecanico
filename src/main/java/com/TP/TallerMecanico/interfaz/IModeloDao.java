package com.TP.TallerMecanico.interfaz;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IModeloDao extends CrudRepository<Modelo, Long>{
    
    @Modifying
    @Query("UPDATE Modelo m SET m.estado = false WHERE m.idModelo = :idModelo") //Query para el Soft Delete
    void marcarComoEliminado(@Param("idModelo") Long idModelo);
    
    List<Modelo> findByEstadoTrue(); //arma una consulta SQL personalizada con estado

    @Modifying
    @Query("UPDATE Modelo m SET m.estado = true WHERE m.idModelo = :idModelo")  //Update a estado true
    void marcarComoActivo(@Param("idModelo") Long idModelo);

    Modelo findByNombreAndMarca(String nombreModelo, Marca marca);

    Modelo findByNombreAndMarcaAndEstadoTrue(String nombreModelo, Marca marca);
    

    Modelo findByNombreAndMarcaAndEstadoFalse(String nombreModelo, Marca marca);
    
    Modelo findByNombreAndEstadoTrue(String nombreModelo);

    Modelo findByMarca(Marca marca);

    List<Modelo> findByMarcaAndEstadoTrue(Marca marca);

}
