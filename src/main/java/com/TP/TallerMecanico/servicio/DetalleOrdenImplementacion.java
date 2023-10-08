package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.interfaz.IDetalleOrdenDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetalleOrdenImplementacion implements IDetalleOrdenService {

    @Autowired
    private IDetalleOrdenDao detalleOrdenDao;

    @Override
    @Transactional(readOnly = true)
    public List<DetalleOrden> listarDetallesOrden() {
        return detalleOrdenDao.findByEstadoTrue();
    }

    @Override
    @Transactional
    public List<DetalleOrden> listarDetallesPorOrden(Orden orden){
        return detalleOrdenDao.findByOrden(orden);
    }

    @Override
    @Transactional //Anotacion para controlar que las operaciones se ejecuten de manera correcta
    public void guardar(DetalleOrden detalleOrden) {    //Metodo para guardar un nuevo tecnico

        //Setteamos los valores ingresados de nombre y apellido en mayusculas

        //Creamos 3 variables de entorno para guardar el dni del cliente, y para buscar y guardar clientes
        //en base al dni y al estad

        Long Id = detalleOrden.getIdDetalleOrden();
        detalleOrden.setSubtotal(detalleOrden.calcularSubtotal());
        //Metodos de clienteDao, que se encargan de comunicarse con la base de datos para obtener (o no) un objeto cliente especifico
        DetalleOrden detalleExistente = detalleOrdenDao.findByIdDetalleOrden(Id);
        DetalleOrden detalleActivado = detalleOrdenDao.findByIdDetalleOrdenAndEstadoTrue(Id);

        //Aca empieza la logica del guardado

        //Si el DNI ingresado no existe en la BD se guarda el cliente
        if (detalleExistente == null) {
            detalleOrdenDao.save(detalleOrden);
            //Caso contrario
        } else {

            //Verificamos si el orden con la misma patente se encuentra activado en la BD
            if (detalleActivado == null) {

                //Llamamos al metodo marcarComoActivo de ordenDao para cambiar su estado a True
                detalleOrdenDao.marcarComoActivo(detalleExistente.getIdDetalleOrden());

                //Metodo para sobreescribir un orden eliminado con datos diferentes a los cargados
                if (!detalleExistente.equals(detalleOrden)){
                    detalleOrden.setIdDetalleOrden(detalleExistente.getIdDetalleOrden());
                    detalleOrdenDao.save(detalleOrden);
                }
            }
        }
    }

    @Override
    @Transactional
    public void actualizar(DetalleOrden detalleOrden){ //Metodo para actualizar un detalleOrden existente activado

        
        detalleOrdenDao.marcarComoActivo(detalleOrden.getIdDetalleOrden());
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

}