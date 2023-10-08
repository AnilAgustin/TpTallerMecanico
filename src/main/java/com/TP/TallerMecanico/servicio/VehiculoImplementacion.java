package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.interfaz.IVehiculoDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehiculoImplementacion implements IVehiculoService {

    //A continuacion se instancia una interfaz, gracias al @Autowired de Spring, podemos instanciar clases abstractas e inyectarle
    //los metodos de una clase que implemente esta interfaz

    @Autowired
    private IVehiculoDao vehiculoDao;

    //A continuacion todos los metodos de la clase


    @Override
    @Transactional(readOnly = true)
    //Metodo para listar todos los vehiculos activos
    public List<Vehiculo> listarVehiculos() { 
        return vehiculoDao.findByEstadoTrue(); //Devuelve una lista de vehiculos
    }

    @Override
    @Transactional  //Anotacion para controlar que las operaciones se ejecuten de manera correcta
    public void guardar(Vehiculo vehiculo) {    //Metodo para guardar un nuevo vehiculo
        
        //Setteamos el valor ingresado como patente en mayusculas
        vehiculo.setPatente(vehiculo.getPatente().toUpperCase());

        //Creamos 3 variables de entorno para guardar la patente,  y para buscar y guardar vehiculos
        //en base a la patente y al estado
        String patente = vehiculo.getPatente();

        //Metodos de vehiculoDao, que se encargan de comunicarse con la base de datos
        Vehiculo patenteExistente = vehiculoDao.findByPatente(patente);
        Vehiculo vehiculoActivado = vehiculoDao.findByPatenteAndEstadoTrue(patente);

        //Aca empieza la logica del guardado

        //Si la patente ingresada no existe en la BD se guarda directamente
        if (patenteExistente == null) { 
            vehiculoDao.save(vehiculo);

        //Caso contrario
        } else {

            //Verificamos si el vehiculo con la misma patente se encuentra activado en la BD
            if (vehiculoActivado == null) {

                //Llamamos al metodo marcarComoActivo de vehiculoDao para cambiar su estado a True
                vehiculoDao.marcarComoActivo(patenteExistente.getIdVehiculo());
                
                //Metodo para sobreescribir un vehiculo eliminado con datos diferentes a los cargados
                if (!patenteExistente.equals(vehiculo)){
                    vehiculo.setIdVehiculo(patenteExistente.getIdVehiculo());
                    vehiculoDao.save(vehiculo);
                }
            }
        }
    } 

    @Override
    @Transactional
    public void actualizar(Vehiculo vehiculo){ //Metodo para actualizar un vehiculo existente

        //Setteamos el valor ingresado como patente en mayusculas
        vehiculo.setPatente(vehiculo.getPatente().toUpperCase());

        //Creamos dos variables de entorno, en una guardamos el Id del vehiculo, y en
        //y en la otra hacemos busqueda por Id y por patente, respectivamente
        Long vehiculoId = vehiculo.getIdVehiculo();
        Vehiculo vehiculoExistente = vehiculoDao.findById(vehiculoId).orElse(null);
        Vehiculo vehiculoByPatente = vehiculoDao.findByPatente(vehiculo.getPatente());
        
        //En el caso de que la patente ingresado ya exista en la base de datos y se encuentre en estado eliminado(false)
        //lo activamos
        if (vehiculoByPatente != null){
            vehiculoDao.marcarComoActivo(vehiculoByPatente.getIdVehiculo());
        }

        //Aca empieza la logica del actualizar

        //Si ya existe un vehiculo en la BD con ese id (solo en casos especiales no se cumplirira)
        if (vehiculoExistente != null) {
            //Guardamos los nuevos datos cargados por el usuario
            String nuevaPatente = vehiculo.getPatente();
            String patenteExistente = vehiculoExistente.getPatente();
            //Controlamos que la patente nuevo sea igual al existente, o que la patente nuevo no exista en la base de datos
            if (nuevaPatente.equals(patenteExistente) || !patenteExisteEnBaseDeDatos(nuevaPatente)) {
                //Guardamos el vehiculo
                vehiculoDao.save(vehiculo);
            }
        }
    }

    //Metodo boolean que nos sirve para simplificar el chequeo realizado en la linea 96
    private boolean patenteExisteEnBaseDeDatos(String patente) {
        //Devuelve true si encuentra un objeto
        return vehiculoDao.findByPatente(patente) != null;
    }


    @Override
    @Transactional
    public void eliminar(Vehiculo vehiculo) { //Metodo para eliminar un vehiculo (borrado logico)
        
        //Llamamos al metodo marcarComoEliminado del vehiculoDao para cambiar el estado del vehiculo a false
        vehiculoDao.marcarComoEliminado(vehiculo.getIdVehiculo());
    }

    @Override
    @Transactional(readOnly = true)
    //Metodo para buscar un vehiculo en base a su id
    public Vehiculo buscarVehiculo(Long vehiculo) {
        return vehiculoDao.findById(vehiculo).orElse(null);
    }

    @Override
    //Metodo para activar un vehiculo
    public void activarVehiculo(Vehiculo vehiculo) {

        //Llamamos al metodo marcarComoActivo del vehiculoDao para cambiar el estado del vehiculo a true
        vehiculoDao.marcarComoActivo(vehiculo.getIdVehiculo());
    }
}