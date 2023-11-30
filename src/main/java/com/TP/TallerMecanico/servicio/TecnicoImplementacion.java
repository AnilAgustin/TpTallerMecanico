package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.interfaz.ITecnicoDao;

import java.util.ArrayList;
import java.util.List;
import com.TP.TallerMecanico.interfaz.IVehiculoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TecnicoImplementacion implements ITecnicoService {

    //A continuacion se instancian interfaces, gracias al @Autowired de Spring, podemos instanciar clases abstractas e inyectarle
    //los metodos de una clase que implemente esta interfaz

    @Autowired
    private ITecnicoDao tecnicoDao;

    //A continuacion todos los metodos de la clase


    @Override
    @Transactional(readOnly = true)
    //Metodo para listar todos los tecnicos activos
    public List<Tecnico> listarTecnicos() { 
        return tecnicoDao.findByEstadoTrue(); //Devuelve una lista de tecnicos 
    }


    @Override
    @Transactional
    public List<Tecnico> filtrarTecnicos(String nombre, String apellido, String legajo){

        if (nombre != null && apellido != null && legajo != null) {
            // Búsqueda por nombre, apellido y legajo
            return tecnicoDao.filtrarTecnicoPorNombreYApellidoYLegajo(nombre, apellido, legajo);
        } else if (nombre != null && apellido != null) {
            // Búsqueda por nombre y apellido
            return tecnicoDao.filtrarTecnicoPorNombreYApellido(nombre, apellido);
        } else if (nombre != null && legajo != null) {
            // Búsqueda por nombre y legajo
            return tecnicoDao.filtrarTecnicoPorNombreYLegajo(nombre, legajo);
        } else if (apellido != null && legajo != null) {
            // Búsqueda por apellido y legajo
            return tecnicoDao.filtrarTecnicoPorApellidoYLegajo(apellido, legajo);
        } else if (nombre != null) {
            // Búsqueda por nombre
            return tecnicoDao.filtrarTecnicoPorNombre(nombre);
        } else if (apellido != null) {
            // Búsqueda por apellido
            return tecnicoDao.filtrarTecnicoPorApellido(apellido);
        } else if (legajo != null) {
            // Búsqueda por legajo
            return tecnicoDao.filtrarTecnicoPorLegajo(legajo);
        }

        return new ArrayList<>(); 
    }

    @Override
    @Transactional //Anotacion para controlar que las operaciones se ejecuten de manera correcta
    public void guardar(Tecnico tecnico) { //Metodo para guardar un nuevo tecnico

        //Setteamos los valores ingresados de nombre y apellido en mayusculas
        tecnico.setNombre(tecnico.getNombre().toUpperCase());
        tecnico.setApellido(tecnico.getApellido().toUpperCase());
        
        //Creamos 3 variables de entorno para guardar el legajo del tecnico, y para buscar y guardar tecnicos
        //en base al legajo y al estado
        String legajo = tecnico.getLegajo();

        //Metodos de tecnicoDao, que se encargan de comunicarse con la base de datos para obtener (o no) un objeto tecnico especifico
        Tecnico legajoExistente = tecnicoDao.findByLegajo(legajo);
        Tecnico legajoActivado = tecnicoDao.findByLegajoAndEstadoTrue(legajo);

        //Aca empieza la logica del guardado
        
        //Si el Legajo ingresado no existe en la BD se guarda el tecnico
        if (legajoExistente == null) {
            tecnicoDao.save(tecnico);

        //Caso contrario
        } else {

            //Verificamos si el tecnico con el mismo Legajo se encuentra activado en la base de datos
            if (legajoActivado == null){

                //Llamamos al metodo activarTecnico
                activarTecnico(legajoExistente);
                
                //Metodo para sobreescribir un tecnico eliminado con datos diferentes a los cargados
                if (!legajoExistente.equals(tecnico)){
                    tecnico.setIdTecnico(legajoExistente.getIdTecnico());
                    tecnicoDao.save(tecnico);
                }
            }
        }
    }


    @Override
    @Transactional
    public void actualizar(Tecnico tecnico){//Metodo para actualizar un tecnico existente activado
        
        //Setteamos los valores ingresados de nombre y apellido en mayusculas
        tecnico.setNombre(tecnico.getNombre().toUpperCase());
        tecnico.setApellido(tecnico.getApellido().toUpperCase());

        //Creamos 3 variables de entorno, en una guardamos el Id del tecnico, y en las otras hacemos
        //busquedas por id y por legajo, respectivamente
        Long tecnicoId = tecnico.getIdTecnico();

        //Metodos de tecnicoDao, que se encargan de comunicarse con la base de datos para obtener (o no) un objeto tecnico especifico
        Tecnico tecnicoExistente = tecnicoDao.findById(tecnicoId).orElse(null);
        Tecnico tecnicoByLegajo = tecnicoDao.findByLegajo(tecnico.getLegajo());
        
        //En el caso de que el legajo ingresado ya exista en la base de datos y se encuentre en estado eliminado(false)
        //lo activamos
        if (tecnicoByLegajo != null){
            activarTecnico(tecnicoByLegajo);
        }
        
        //Aca empieza la logica del actualizar

        //Si ya existe un tecnico en la BD con ese id (solo en casos especiales no se cumplirira)
        if (tecnicoExistente != null) {
            
            //Guardamos los nuevos datos cargados por el usuario
            String nuevoLegajo = tecnico.getLegajo();
            String legajoExistente = tecnicoExistente.getLegajo();

            //Controlamos que el legajo nuevo sea igual al existente, o que el legajo nuevo no exista en la base de datos
            if (nuevoLegajo.equals(legajoExistente) || !legajoExisteEnBaseDeDatos(nuevoLegajo)) {
                //Guardamos el tecnico
                tecnicoDao.save(tecnico);
            }
        }
    }   

    //Metodo boolean que nos sirve para simplificar el chequeo realizado en la linea 109
    private boolean legajoExisteEnBaseDeDatos(String legajo) {

        //Devuelve true si encuentra un objeto
        return tecnicoDao.findByLegajo(legajo) != null;
    }

    @Override
    @Transactional
    public void eliminar(Tecnico tecnico) { //Metodo para eliminar un tecnico (borrado logico), con la logica para que sea en cascada
        
        //Llamamos al metodo marcarComoEliminado del tecnicoDao para cambiar el estado del tecnico a false
        tecnicoDao.marcarComoEliminado(tecnico.getIdTecnico());

    }


    @Override
    @Transactional(readOnly = true)
    //Metodo para buscar un tecnico en base a su id
    public Tecnico buscarTecnico(Long idTecnico) {
        return tecnicoDao.findById(idTecnico).orElse(null);
    }

    @Override
    @Transactional
    //Metodo para activar un tecnico
    public void activarTecnico(Tecnico tecnico){

        //Llamamos al metodo marcarComoActivo del tecnicoDao para cambiar el estado del tecnico a true
        tecnicoDao.marcarComoActivo(tecnico.getIdTecnico());
    }
}