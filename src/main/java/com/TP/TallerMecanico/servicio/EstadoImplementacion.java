package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Estado;
import com.TP.TallerMecanico.interfaz.IEstadoDao;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoImplementacion implements IEstadoService {

    //A continuacion se instancian interfaces, gracias al @Autowired de Spring, podemos instanciar clases abstractas e inyectarle
    //los metodos de una clase que implemente esta interfaz

    @Autowired
    private IEstadoDao estadoDao;

    //A continuacion todos los metodos de la clase

    @Override
    public List<Estado> listarEstados() {
        List<Estado> estadosPosibles = new ArrayList<>();

        Estado estadoEnProgreso = estadoDao.findByNombre("EN PROGRESO"); //Estado en el que se pone la orden por defecto
        Estado estadoEnEspera = estadoDao.findByNombre("EN ESPERA");
        Estado estadoCompletada = estadoDao.findByNombre("COMPLETADA");

        if (estadoEnEspera == null){
            Estado enEspera = new Estado("EN ESPERA");
            estadoDao.save(enEspera);
            estadosPosibles.add(enEspera);
        } else {
            estadosPosibles.add(estadoEnEspera);
        }
        if (estadoCompletada == null){
            Estado completada = new Estado("COMPLETADA");
            estadoDao.save(completada);
            estadosPosibles.add(completada);
        } else {
            estadosPosibles.add(estadoCompletada);
        }
        if (estadoEnProgreso == null){
            Estado enProgreso = new Estado("EN PROGRESO");
            estadoDao.save(enProgreso);
            estadosPosibles.add(enProgreso);
        } else {
            estadosPosibles.add(estadoEnProgreso);
        }
        return estadosPosibles;
    }

    @Override
    public void guardar(Estado estado) {
        estado.setNombre(estado.getNombre().toUpperCase());
        String nombreNuevoEstado = estado.getNombre();

        if (estadoDao.findByNombre(nombreNuevoEstado) == null){
            estadoDao.save(estado);
        }
    }
}