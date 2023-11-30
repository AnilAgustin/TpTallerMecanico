package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Servicio;
import com.TP.TallerMecanico.interfaz.IServicioDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioImplementacion implements IServicioService {

    //A continuacion se instancian interfaces, gracias al @Autowired de Spring, podemos instanciar clases abstractas e inyectarle
    //los metodos de una clase que implemente esta interfaz

    @Autowired
    private IServicioDao servicioDao;

    @Override
    @Transactional
    public List<Servicio> filtrarServicios(String nombre){
        return servicioDao.filtrarPorNombreYEstadoTrue(nombre);
    }


    @Override
    @Transactional(readOnly = true)
    //Metodo para listar todos los servicios activos
    public List<Servicio> listarServicios() {
        return servicioDao.findByEstadoTrue(); //Devuelve una lista de servicios
    }


    @Override
    @Transactional  //Anotacion para controlar que las operaciones se ejecuten de manera correcta
    public void guardar(Servicio servicio) {  //Metodo para guardar una nueva servicio
        
        //Setteamos el nombre en mayusculas
        servicio.setNombre(servicio.getNombre().toUpperCase());

        //Creamos 3 variables de entorno para guarar el nombre del servicio, y para buscar y guardar servicios
        //en base al nombre y al estado
        String nombreServicio = servicio.getNombre();

        //Metodos de servicioDao, que se encargan de comunicarse con la base de datos para obtener (o no) un objeto servicio especifico
        Servicio servicioExistente = servicioDao.findByNombre(nombreServicio);
        Servicio servicioActivada = servicioDao.findByNombreAndEstadoTrue(nombreServicio);

        //Aca empieza la logica del guardado

        //Controlamos que el nombre no este con caracteres vacios
        if (!nombreServicio.trim().isEmpty()) {//Para que no se ingresen solamente caracters vacios
            
            //Si el nombre ingresado no existe en la BD se guarda la servicio
            if (servicioExistente == null) {
                servicioDao.save(servicio);

            //Caso contrario
            } else {


                //Verificamos si la servicio con el mismo nombre se encuentra activada en la base de datos
                if (servicioActivada == null) {

                    //Llamamos al metodo activarServicio
                    activarServicio(servicioExistente);
                    if (!servicioExistente.equals(servicio)){
                        servicio.setIdServicio(servicioExistente.getIdServicio());
                        servicioDao.save(servicio);
                    }
                }
            }
        }
    }


    @Override
    @Transactional
    public void actualizar(Servicio servicio){    //Metodo para actualizar una servicio existente activado
        
        //Setteamos el nombre ingresado en mayusculas
        servicio.setNombre(servicio.getNombre().toUpperCase());

        //Creamos 4 variables de entorno, en una guardamos el Id de la servicio, en otra el
        //nombre nuevo de la servicio, y en las otras hacemos busquedas por id y por nombre, respectivamente
        Long servicioId = servicio.getIdServicio();
        String nuevoNombre = servicio.getNombre();
        
        //Metodos de servicioDao que se encargan de comunicarse con la base de datos para obtener (o no) un objeto servicio especifico
        Servicio servicioExistente = servicioDao.findById(servicioId).orElse(null);
        Servicio servicioExistenteByNombreAndEstado = servicioDao.findByNombreAndEstadoFalse(nuevoNombre);

        //En el caso de que el nombre ingresado ya exista en la base de datos y se encuentre en estado eliminado(false)
        //lo activamos
        if (servicioExistenteByNombreAndEstado!=null) {
            activarServicio(servicioExistenteByNombreAndEstado);
        }

        //Aca empieza la logica del actualizar 

        //Si ya existe una servicio en la BD con ese id (solo en casos especiales no se cumplirira)
        if(servicioExistente != null){           
            
            //Guardamos el nuevo nombre cargado por el usuario 
            String nombreExistente = servicioExistente.getNombre();
 
            //Controlamos que el nombre nuevo sea igual al existente, o que no exista en la base de datos
            if(nuevoNombre.equals(nombreExistente) || !nombreExisteEnBaseDeDatos(nuevoNombre)){
                //Guardamos la servicio
                servicioDao.save(servicio);
            }
        }
    }

    //Metodo boolean que nos sirve para simplificar el chequeo realizado en la linea 105
    private boolean nombreExisteEnBaseDeDatos(String nombreServicio) {

        //Devuelve true si encuentra un objeto
        return servicioDao.findByNombre(nombreServicio) != null;
    }

    @Override
    @Transactional
    public void eliminar(Servicio servicio) { //Metodo para eliminar una servicio (borrado logico), con la logica para que sea en cascada
        
        //Llamamos al metodo serviciorComoEliminado de la servicioDao para cambiar el estado de la servicio a false
        servicioDao.marcarComoEliminado(servicio.getIdServicio());

        
    }

    @Override
    @Transactional(readOnly = true)
    //Metodo para buscar una servicio en base a su id
    public Servicio buscarServicio(Servicio servicio) {
        return servicioDao.findById(servicio.getIdServicio()).orElse(null);
    }

    @Override
    @Transactional
    //Metodo para activar una servicio
    public void activarServicio(Servicio servicio){

        //Llamamos al metodo serviciorComoActivo de la servicioDao para cambiar el estado de la servicio a true
        servicioDao.marcarComoActivo(servicio.getIdServicio());


    }
}