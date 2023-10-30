package com.TP.TallerMecanico.servicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.entidad.Tecnico;
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
    public List<Orden> listarOrdenesTecnico(Tecnico tecnico){
        return ordenDao.findByTecnicoAndEstadoTrue(tecnico);
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
    public List<Orden> filtrarOrdenes(Long marcaId, Long modeloId, Long numero, LocalDate fechaDocumento) {
        if (marcaId != -1 && modeloId != -1 && numero != null && fechaDocumento != null) {
            // Búsqueda para marca, modelo, número y fecha
            return ordenDao.filtrarOrdenPorMarcaYModeloYNumeroYFechaDocumento(marcaId, modeloId, numero, fechaDocumento);
        } else if (marcaId != -1 && modeloId != -1 && numero != null && fechaDocumento == null) {
            // Búsqueda para marca, modelo y número
            return ordenDao.filtrarOrdenPorMarcaYModeloYNumero(marcaId, modeloId, numero);
        } else if (marcaId != -1 && modeloId != -1 && numero == null && fechaDocumento != null) {
            // Búsqueda para marca, modelo y fecha
            return ordenDao.filtrarOrdenPorMarcaYModeloYFechaDocumento(marcaId, modeloId, fechaDocumento);
        } else if (marcaId != -1 && modeloId == -1 && numero != null && fechaDocumento != null) {
            // Búsqueda para marca, número y fecha
            return ordenDao.filtrarOrdenPorMarcaYNumeroYFechaDocumento(marcaId, numero, fechaDocumento);
        } else if (marcaId == -1 && modeloId != -1 && numero != null && fechaDocumento != null) {
            // Búsqueda para modelo, número y fecha
            return ordenDao.filtrarOrdenPorModeloYNumeroYFechaDocumento(modeloId, numero, fechaDocumento);
        } else if (marcaId != -1 && modeloId != -1 && numero == null && fechaDocumento == null) {
            // Búsqueda para marca y modelo
            return ordenDao.filtrarOrdenPorMarcaYModelo(marcaId, modeloId);
        } else if (marcaId != -1 && modeloId == -1 && numero != null && fechaDocumento == null) {
            // Búsqueda para marca y número
            return ordenDao.filtrarOrdenPorMarcaYNumero(marcaId, numero);
        } else if (marcaId == -1 && modeloId != -1 && numero != null && fechaDocumento == null) {
            // Búsqueda para modelo y número
            return ordenDao.filtrarOrdenPorModeloYNumero(modeloId, numero);
        } else if (marcaId != -1 && modeloId == -1 && numero == null && fechaDocumento != null) {
            // Búsqueda para marca y fecha
            return ordenDao.filtrarOrdenPorMarcaYFechaDocumento(marcaId, fechaDocumento);
        } else if (modeloId != -1 && numero == null && fechaDocumento != null) {
            // Búsqueda para modelo y fecha
            return ordenDao.filtrarOrdenPorModeloYFechaDocumento(modeloId, fechaDocumento);
        } else if (numero != null && fechaDocumento != null) {
            // Búsqueda por número y fecha
            return ordenDao.filtrarOrdenPorNumeroYFechaDocumento(numero, fechaDocumento);
        } else if (numero != null) {
            // Búsqueda por número
            return ordenDao.filtrarOrdenPorNumero(numero);
        } else if (marcaId != -1) {
            // Filtrar solo por marca
            return ordenDao.filtrarOrdenPorMarca(marcaId);
        } else if (modeloId != -1) {
            // Filtrar solo por modelo
            return ordenDao.filtrarOrdenPorModelo(modeloId);
        } else if (fechaDocumento != null) {
            // Filtrar por fecha
            return ordenDao.filtrarOrdenPorFecha(fechaDocumento);
        } else if (marcaId == -1 && modeloId == -1 && numero == null && fechaDocumento == null) {
            // Listar todas las órdenes
            return listarOrdenes();
        }

        return new ArrayList<>();
    }

    @Override
    @Transactional   
    public void actualizarKilometraje(Orden orden){
        Long ordenId = orden.getIdOrden();

        Orden ordenViejo = ordenDao.findByIdOrdenAndEstadoTrue(ordenId);

        String kilometrajeNuevo = orden.getKilometros();
        String kilometrajeViejo = ordenViejo.getKilometros();

        if (Integer.parseInt(kilometrajeNuevo)> Integer.parseInt(kilometrajeViejo)) {
            ordenViejo.setKilometros(kilometrajeNuevo);
        }
    }
}