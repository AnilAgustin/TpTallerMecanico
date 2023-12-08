package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.entidad.Servicio;
import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.interfaz.IDetalleOrdenDao;
import java.util.List;
import java.util.Optional;

import com.TP.TallerMecanico.interfaz.IOrdenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetalleOrdenImplementacion implements IDetalleOrdenService {

    @Autowired
    private IDetalleOrdenDao detalleOrdenDao;

    @Autowired
    private IOrdenDao ordenDao;

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrden> listarDetallesOrden() {
        return detalleOrdenDao.findByEstadoTrue();
    }

    @Override
    @Transactional
    public List<DetalleOrden> listarDetallesPorOrden(Orden orden){
        return detalleOrdenDao.findByOrdenAndEstadoTrue(orden);
    }

    @Override
    @Transactional //Anotacion para controlar que las operaciones se ejecuten de manera correcta
    public void guardar(DetalleOrden detalleOrden) {    //Metodo para guardar un nuevo tecnico
        
        detalleOrden.setSubtotal(detalleOrden.calcularSubtotal());
        Servicio nuevoServicio = detalleOrden.getServicio();
        Orden ordenDetalleOrden = detalleOrden.getOrden();
        DetalleOrden detalleByServicioAndOrden = detalleOrdenDao.findByServicioAndOrden(nuevoServicio, ordenDetalleOrden);
        DetalleOrden detalleOrdenActivado = detalleOrdenDao.findByServicioAndOrdenAndEstadoTrue(nuevoServicio, ordenDetalleOrden);

        if (detalleByServicioAndOrden == null) {
            detalleOrdenDao.save(detalleOrden);
        } else {
            if ((detalleOrdenActivado) == null) {

                detalleOrdenDao.marcarComoActivo(detalleByServicioAndOrden.getIdDetalleOrden());

                if (!detalleByServicioAndOrden.equals(detalleOrden)) {
                    detalleOrden.setIdDetalleOrden(detalleByServicioAndOrden.getIdDetalleOrden());
                    detalleOrdenDao.save(detalleOrden);
                }
            }
        }
    }

    @Override
    @Transactional
    public void actualizar(DetalleOrden detalleOrden, Long idOrden){ //Metodo para actualizar un detalleOrden existente activado
        //Aca empieza la logica del actualizar
        detalleOrden.setOrden(ordenDao.findByIdOrden(idOrden));
        Servicio nuevoServicio = detalleOrden.getServicio();
        Long detalleOrdenId = detalleOrden.getIdDetalleOrden();
        DetalleOrden servicioExistente = detalleOrdenDao.findById(detalleOrdenId).orElse(null);
        DetalleOrden detalleByServicioAndOrden = detalleOrdenDao.findByServicioAndOrden(nuevoServicio, detalleOrden.getOrden());

        //CHEQUEO POR SERVICIO Y ORDEN PORQUE EL SERVICIO PUEDE EXISTIR EN OTRO DETALLE DE OTRA ORDEN
        //Y ME DEVOLVERIA UN RESULTADO

        if (detalleByServicioAndOrden != null) {
            detalleOrdenDao.marcarComoActivo(detalleByServicioAndOrden.getIdDetalleOrden());
        }

        if (servicioExistente != null){

            if (nuevoServicio.equals(servicioExistente.getServicio()) || detalleByServicioAndOrden==null) {
                detalleOrden.setSubtotal(detalleOrden.calcularSubtotal());
                detalleOrdenDao.save(detalleOrden);
            }
        }
    }

    @Override
    @Transactional
    public void activarDetalleOrden(DetalleOrden detalleOrden){
        detalleOrdenDao.marcarComoActivo(detalleOrden.getIdDetalleOrden());

    }

    @Override
    @Transactional
    public void eliminar(DetalleOrden detalleOrden) {
        detalleOrdenDao.marcarComoEliminado(detalleOrden.getIdDetalleOrden());
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleOrden buscarDetalleOrden(Long detalleOrden) {
        return detalleOrdenDao.findById(detalleOrden).orElse(null);
    }

    @Override
    @Transactional
    public List<DetalleOrden> findByIdOrden(Long idOrden){
        return detalleOrdenDao.findbyIdOrden(idOrden);
    }


}