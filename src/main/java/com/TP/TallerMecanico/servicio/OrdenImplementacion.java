package com.TP.TallerMecanico.servicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.interfaz.IOrdenDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdenImplementacion implements IOrdenService {

    //A continuacion se instancia una interfaz, gracias al @Autowired de Spring, podemos instanciar clases abstractas e inyectarle
    //los metodos de una clase que implemente esta interfaz

    @Autowired
    private IOrdenDao ordenDao;

    //A continuacion todos los metodos de la clase
    @Override
    @Transactional(readOnly = true)
    public List<Orden> listarOrdenesFecha(LocalDate fechaOrden){
        return ordenDao.findByFechaRegistro(fechaOrden);
    }

    @Override
    @Transactional(readOnly = true)
    //Metodo para listar todos los ordenes activos
    public List<Orden> listarOrdenes() { 
        return ordenDao.findByEstadoTrue(); //Devuelve una lista de ordenes
    }

    @Override
    @Transactional
    public void guardar(Orden orden) {
        orden.setFechaRegistro(LocalDate.now());
        ordenDao.save(orden);
    }

    @Override
    @Transactional
    public void actualizar(Orden orden){
        orden.setFechaRegistro(LocalDate.now());
        ordenDao.save(orden);
    }

    @Override
    @Transactional
    public void eliminar(Orden orden) { //Metodo para eliminar un orden (borrado logico)
        ordenDao.marcarComoEliminado(orden.getIdOrden());
    }

    @Override
    @Transactional(readOnly = true)
    //Metodo para buscar un orden en base a su id
    public Orden buscarOrden(Long orden) {
        return ordenDao.findById(orden).orElse(null);
    }

    @Override
    //Metodo para activar un orden
    public void activarOrden(Orden orden) {

        //Llamamos al metodo marcarComoActivo del ordenDao para cambiar el estado del orden a true
        ordenDao.marcarComoActivo(orden.getIdOrden());
    }

    @Override
    @Transactional
    public List<Orden> filtrarOrdenes(Long marcaId, Long modeloId,Long numero){
        if (marcaId!= -1 && modeloId != -1 && numero!=null) {
            //Busqueda para marca, modelo y numero
            return ordenDao.filtrarVehiculoPorMarcaYModeloYNumero(marcaId, modeloId, numero);

        }else if (marcaId != -1 && modeloId !=-1 && numero==null) {
            //Busqueda para marca y modelo
            return ordenDao.filtrarVehiculoPorMarcaYModelo(marcaId, modeloId);
        }
        else if (marcaId != -1 && modeloId ==-1 && numero!=null) {
            //Busqueda para marca y numero
            return ordenDao.filtrarVehiculoPorMarcaYNumero(marcaId,numero);

        }else if (marcaId ==-1 && modeloId!=-1 && numero!=null) {
            //Busqueda para modelo y numero
            return ordenDao.filtrarVehiculoPorModeloYNumero(modeloId, numero);

        }else if (numero!=null) {
            //Busqueda por numero
            return ordenDao.filtrarVehiculoPorNumero(numero);
        }else if (marcaId != -1) {
            //Filtrar solo por marca
            return ordenDao.filtrarVehiculoPorMarca(marcaId);
        } else if (modeloId != -1) {
            //Filtrar solo por modelo
            return ordenDao.filtrarVehiculoPorModelo(modeloId);
        }else if (marcaId == -1 && modeloId == -1 && numero==null) {
            return listarOrdenes();
        }

        return new ArrayList<>();
    }
}