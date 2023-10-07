package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.interfaz.IOrdenDao;
import java.util.List;
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
    //Metodo para listar todos los ordenes activos
    public List<Orden> listarOrdenes() { 
        return ordenDao.findByEstadoTrue(); //Devuelve una lista de ordenes
    }

    @Override
    @Transactional  //Anotacion para controlar que las operaciones se ejecuten de manera correcta
    public void guardar(Orden orden) {    //Metodo para guardar un nuevo orden
        

        Long Id = orden.getIdOrden();
        //Metodos de ordenDao, que se encargan de comunicarse con la base de datos
        Orden IdExistente = ordenDao.findByIdOrden(Id);
        Orden ordenActivado = ordenDao.findByIdOrdenAndEstadoTrue(Id);

        //Aca empieza la logica del guardado

        //Si la patente ingresada no existe en la BD se guarda directamente
        if (IdExistente == null) { 
            ordenDao.save(orden);

        //Caso contrario
        } else {

            //Verificamos si el orden con la misma patente se encuentra activado en la BD
            if (ordenActivado == null) {

                //Llamamos al metodo marcarComoActivo de ordenDao para cambiar su estado a True
                ordenDao.marcarComoActivo(IdExistente.getIdOrden());
                
                //Metodo para sobreescribir un orden eliminado con datos diferentes a los cargados
                if (!IdExistente.equals(orden)){
                    orden.setIdOrden(IdExistente.getIdOrden());
                    ordenDao.save(orden);
                }
            }
        }
    } 

    @Override
    @Transactional
    public void actualizar(Orden orden){ //Metodo para actualizar un orden existente

        //Setteamos el valor ingresado como patente en mayusculas
        // orden.setIdOrden(orden.getIdOrden());

        //Creamos dos variables de entorno, en una guardamos el Id del orden, y en
        //y en la otra hacemos busqueda por Id y por patente, respectivamente

        
        //En el caso de que la patente ingresado ya exista en la base de datos y se encuentre en estado eliminado(false)
        //lo activamos
        ordenDao.marcarComoActivo(orden.getIdOrden());

        //Aca empieza la logica del actualizar

    }




    @Override
    @Transactional
    public void eliminar(Orden orden) { //Metodo para eliminar un orden (borrado logico)
        
        //Llamamos al metodo marcarComoEliminado del ordenDao para cambiar el estado del orden a false
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
}