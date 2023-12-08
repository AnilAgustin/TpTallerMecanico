package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.interfaz.IMarcaDao;
import com.TP.TallerMecanico.interfaz.IModeloDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarcaImplementacion implements IMarcaService {

    //A continuacion se instancian interfaces, gracias al @Autowired de Spring, podemos instanciar clases abstractas e inyectarle
    //los metodos de una clase que implemente esta interfaz

    @Autowired
    private IMarcaDao marcaDao;

    @Autowired
    private IModeloDao modeloDao;

    @Autowired 
    private IModeloService modeloService;

    //A continuacion todos los metodos de la clase


    @Override
    @Transactional(readOnly = true)
    //Metodo para listar todos los marcas activos
    public List<Marca> listarMarcas() {
        return marcaDao.findByEstadoTrue(); //Devuelve una lista de marcas
    }

    @Override
    @Transactional
    public List<Marca> buscarMarcaPorNombre(String nombre){
        return marcaDao.findMarcaByNombreAndEstadoTrue(nombre);
    }

    @Override
    @Transactional  //Anotacion para controlar que las operaciones se ejecuten de manera correcta
    public void guardar(Marca marca) {  //Metodo para guardar una nueva marca
        
        //Setteamos el nombre en mayusculas
        marca.setNombre(marca.getNombre().toUpperCase());

        //Creamos 3 variables de entorno para guarar el nombre de la marca, y para buscar y guardar marcas
        //en base al nombre y al estado
        String nombreMarca = marca.getNombre();

        //Metodos de marcaDao, que se encargan de comunicarse con la base de datos para obtener (o no) un objeto marca especifico
        Marca marcaExistente = marcaDao.findByNombre(nombreMarca);
        Marca marcaActivada = marcaDao.findByNombreAndEstadoTrue(nombreMarca);

        //Aca empieza la logica del guardado

        //Controlamos que el nombre no este con caracteres vacios
        if (!nombreMarca.trim().isEmpty()) {//Para que no se ingresen solamente caracters vacios
            
            //Si el nombre ingresado no existe en la BD se guarda la marca
            if (marcaExistente == null) {
                marcaDao.save(marca);

            //Caso contrario
            } else {
                
                //Verificamos si la marca con el mismo nombre se encuentra activada en la base de datos
                if (marcaActivada == null) {

                    //Llamamos al metodo activarMarca
                    activarMarca(marcaExistente);
                }
            }
        }
    }


    @Override
    @Transactional
    public void actualizar(Marca marca){    //Metodo para actualizar una marca existente activado
        
        //Setteamos el nombre ingresado en mayusculas
        marca.setNombre(marca.getNombre().toUpperCase());

        //Creamos 4 variables de entorno, en una guardamos el Id de la marca, en otra el
        //nombre nuevo de la marca, y en las otras hacemos busquedas por id y por nombre, respectivamente
        Long marcaId = marca.getIdMarca();
        String nuevoNombre = marca.getNombre();
        
        //Metodos de marcaDao que se encargan de comunicarse con la base de datos para obtener (o no) un objeto marca especifico
        Marca marcaExistente = marcaDao.findById(marcaId).orElse(null);
        Marca marcaExistenteByNombreAndEstado = marcaDao.findByNombreAndEstadoFalse(nuevoNombre);

        //En el caso de que el nombre ingresado ya exista en la base de datos y se encuentre en estado eliminado(false)
        //lo activamos
        if (marcaExistenteByNombreAndEstado!=null) {
            activarMarca(marcaExistenteByNombreAndEstado);
        }

        //Aca empieza la logica del actualizar 

        //Si ya existe una marca en la BD con ese id (solo en casos especiales no se cumplirira)
        if(marcaExistente != null){           
            
            //Guardamos el nuevo nombre cargado por el usuario 
            String nombreExistente = marcaExistente.getNombre();
 
            //Controlamos que el nombre nuevo sea igual al existente, o que no exista en la base de datos
            if(nuevoNombre.equals(nombreExistente) || !nombreExisteEnBaseDeDatos(nuevoNombre)){
                //Guardamos la marca
                marcaDao.save(marca);
            }
        }
    }

    //Metodo boolean que nos sirve para simplificar el chequeo realizado en la linea 105
    private boolean nombreExisteEnBaseDeDatos(String nombreMarca) {

        //Devuelve true si encuentra un objeto
        return marcaDao.findByNombre(nombreMarca) != null;
    }

    @Override
    @Transactional
    public void eliminar(Marca marca) { //Metodo para eliminar una marca (borrado logico), con la logica para que sea en cascada
        
        //Llamamos al metodo marcarComoEliminado de la marcaDao para cambiar el estado de la marca a false
        marcaDao.marcarComoEliminado(marca.getIdMarca());

        //Realizamos un ciclo que recorra todos los vehiculos asociados al id de ese marca y llamamos al metodo
        //eliminar de cada vehiculo, el cual ejecutara el mismo metodo marcarComoEliminado de vehiculoDao
        for (Modelo modelo : modeloDao.findByMarcaAndEstadoTrue(marca)) {
            modeloService.eliminar(modelo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    //Metodo para buscar una marca en base a su id
    public Marca buscarMarca(Marca marca) {
        return marcaDao.findById(marca.getIdMarca()).orElse(null);
    }

    @Override
    @Transactional
    //Metodo para activar una marca
    public void activarMarca(Marca marca){

        //Llamamos al metodo marcarComoActivo de la marcaDao para cambiar el estado de la marca a true
        marcaDao.marcarComoActivo(marca.getIdMarca());

        //Realizamos un ciclo que recorra todos los vehiculos asociados al id de ese marca y llamamos al metodo
        //al metodo activar de cada vehiculo, el cual ejecutara el mismo metodo marcarComoActivo de vehiculoDao
        for (Modelo modelo : marca.getModelos()) {
            modeloService.activarModelo(modelo);
        }
    }

    @Override
    @Transactional
    public Marca obtenerMarcaPorId(Long idMarca){
        Marca marca = marcaDao.findById(idMarca).orElse(null);
        return marca;
    }
}