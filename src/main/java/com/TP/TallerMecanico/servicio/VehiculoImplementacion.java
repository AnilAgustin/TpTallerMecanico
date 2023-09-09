package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.interfaz.IVehiculoDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehiculoImplementacion implements IVehiculoService {

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listarVehiculos() { return vehiculoDao.findByEstadoTrue(); }

    @Override
    @Transactional
    public void guardar(Vehiculo vehiculo) {
        String patente = vehiculo.getPatente();
        Long id = vehiculo.getIdVehiculo();
        Vehiculo vehiculoExistente = vehiculoDao.findByIdVehiculo(id);
        Vehiculo patenteExistente = vehiculoDao.findByPatente(patente);
        Vehiculo vehiculoRegistrado = vehiculoDao.findByPatenteAndEstadoTrue(patente);

        if (patenteExistente == null){ //Chequeamos si la patente ya existe, y si no existe guardamos directamente
            vehiculoDao.save(vehiculo);
        } else { //Si la patente existe
            if (vehiculoExistente != null){ //Chequeamos si estamos agregando o editando un vehiculo (comparando por id)
                vehiculoDao.save(vehiculo); //Guardamos el vehiculo (editado)
            }
            if (vehiculoRegistrado != null){
                vehiculoDao.marcarComoActivo(patenteExistente.getIdVehiculo());
            }
        }
    }

    @Override
    @Transactional
    public void eliminar(Vehiculo vehiculo) {
        vehiculoDao.marcarComoEliminado(vehiculo.getIdVehiculo());
    }

    @Override
    @Transactional(readOnly = true)
    public Vehiculo buscarVehiculo(Vehiculo vehiculo) {
        return vehiculoDao.findById(vehiculo.getIdVehiculo()).orElse(null);
    }
}