package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.interfaz.IModeloDao;
import com.TP.TallerMecanico.interfaz.IVehiculoDao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModeloImplementacion implements IModeloService {

    @Autowired
    private IModeloDao modeloDao;

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Autowired
    private IVehiculoService vehiculoService;


    @Override
    @Transactional
    public List<Modelo> filtrarModelos(Long marcaId, String nombre){
        if (marcaId != -1 && nombre!= null) {
            return modeloDao.filtrarModeloPorMarcaYNombre(marcaId, nombre);
        }else if (nombre == null) {
            return modeloDao.filtrarModeloPorMarca(marcaId);
        }else if (marcaId == -1) {
            return modeloDao.filtrarModeloPorNombre(nombre);
        }

        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modelo> listarModelos() {
        return modeloDao.findByEstadoTrue();
    }

    @Override
    @Transactional //Anotacion para controlar que las operaciones se ejecuten de manera correcta
    public void guardar(Modelo modelo) {    //Metodo para guardar un nuevo tecnico
        
        //Setteamos el valor de nombre ingresado a mayusculas
        modelo.setNombre(modelo.getNombre().toUpperCase());

        //Creamos 4 variables de entorno, en un par guardamos nombre y marca, y en los otros
        //dos guardamos dos objetos modelos buscandolos por nombre, marca y estado (true y false respectivamente)
        String nombreNuevo = modelo.getNombre();
        Marca marcaNueva = modelo.getMarca();

        //Metodos de modeloDao que se encargan de comunicarse con la base de datos para obtener (o no) un objeto modelo especifico
        Modelo modeloExistente = modeloDao.findByNombreAndMarcaAndEstadoTrue(nombreNuevo, marcaNueva);
        Modelo modeloActivado = modeloDao.findByNombreAndMarcaAndEstadoFalse(nombreNuevo, marcaNueva);

        //Aca empieza la logica del guardado
        
        //En el caso de modelo, al no tener patente, dni o legajo, lo que lo hace unico es la combinacion entre su nombre
        //y su marca, por eso debemos hacer una verificacion de que esta combinacion se encuentre en ambos estados
        //antes de guardar un modelo nuevo en nuestra base de datos


        //Si el modelo ingresado no existe en la base de datos con estado False
        if (modeloActivado == null) {
            //Si el modelo ingresado no existe en la base de datos con estado True
            if (modeloExistente == null) {
                //Guardamos el modelo
                modeloDao.save(modelo);
            }

        //Caso contrario
        } else {

            //Activamos el modelo, ya que si existe, solo que no se encuentra activo
            activarModelo(modeloActivado);
        }
    }

    @Override
    @Transactional
    public void actualizar(Modelo modelo){ //Metodo para actualizar un modelo existente activado
        
        //Setteamos el valor del nombre en mayusculas
        modelo.setNombre(modelo.getNombre().toUpperCase());

        //Creamos 5 variables de entorno, en las cuales guardamos:

        //La marca nueva ingresada por el usuario
        Marca marcaNueva = modelo.getMarca();
        //El nombre nuevo ingresado por el usuario
        String nombreNuevo = modelo.getNombre();


        //Metodo de modeloDao, que se encarga de comunicarse con la base de datos para obtener (o no), un objeto especifico
        //Buscamos el modelo por Id
        Modelo modeloViejo = modeloDao.findById(modelo.getIdModelo()).orElse(null);
        
        //La marca vieja del modelo en la base de datos
        Marca marcaExistente = modeloViejo.getMarca();
        //El nombre viejo del modelo en la base de datos
        String nombreViejo = modeloViejo.getNombre();

        //Aca empieza la logica del actualizar

        //Si la marca nueva ingresada es igual a la marca vieja
        if (marcaNueva.equals((marcaExistente))){
            
            //Si el nombre nuevo es diferente al nombre viejo
            if (!nombreNuevo.equals((nombreViejo))) {

                //Verificamos que el nombre nuevo y la marca nueva/vieja no exista en la base de datos
                Modelo checkNM1 = modeloDao.findByNombreAndMarca(nombreNuevo, marcaNueva);
                Modelo checkNM2 = modeloDao.findByNombreAndMarcaAndEstadoFalse(nombreNuevo, marcaNueva);
                
                //Activamos el modelo en caso de que el mismo se encuentre eliminado
                
                if (checkNM2 != null) {
                    activarModelo(checkNM2);
                }

                //Hacemos el control de la variable antes definida
                if (checkNM1 == null) {
                    //Si no existe en la BD entonces lo guardamos
                    modeloDao.save(modelo);
                }
            }

        
        //Caso contrario
        } else {

            //Verificamos que el nombre nuevo y la marca nueva no exista en la base de datos
            Modelo checkNM3 = modeloDao.findByNombreAndMarca(nombreNuevo, marcaNueva);
            Modelo checkNM4 = modeloDao.findByNombreAndMarcaAndEstadoFalse(nombreNuevo, marcaNueva);
            
            //Hacemos el control de la variable antes definida
            if (checkNM3 == null){
                
                //Si no existe en la BD entonces lo guardamos
                modeloDao.save(modelo);
            }
            
            if (checkNM4 != null) {
                //Activamos el modelo en caso de que el mismo se encuentre eliminado
                activarModelo(checkNM4);
            }
            
            
        }
    }


    @Override
    @Transactional
    public void activarModelo(Modelo modelo){
        modeloDao.marcarComoActivo(modelo.getIdModelo());
        for (Vehiculo vehiculo : modelo.getVehiculos()) {
            vehiculoService.activarVehiculo(vehiculo);
        }
        
    }

    @Override
    @Transactional
    public void eliminar(Modelo modelo) {
        modeloDao.marcarComoEliminado(modelo.getIdModelo());
        for (Vehiculo vehiculo: vehiculoDao.findByModeloAndEstadoTrue(modelo)){
            vehiculoService.eliminar(vehiculo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Modelo buscarModelo(Modelo modelo) {
        return modeloDao.findById(modelo.getIdModelo()).orElse(null);
    }

    @Override
    @Transactional
    public Modelo obtenerModeloPorId(Long modeloId){
        return modeloDao.findById(modeloId).orElse(null);
    }

}
